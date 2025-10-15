import java.util.Objects;

public class Servidor extends Tarefa {

    private final BufferCircular buffer;
    private final BaseDados db;
    private final RobotLegoEV3Simula robot;

    private volatile boolean ativa = false; // controla loop principal
    private volatile boolean started = false; // garante start() só uma vez

    public Servidor(BufferCircular buffer, BaseDados db) {
        this.buffer = Objects.requireNonNull(buffer);
        this.db = Objects.requireNonNull(db);
        this.robot = new RobotLegoEV3Simula();
    }

    /* ----- API de produtores----- */

    public void Reta(int distancia) {
        Comando c = new Comando("RETA", distancia, 0);
        buffer.inserirElemento(c);
    }

    public void CurvarDireita(int raio, int angulo) {
        Comando c = new Comando("CURVA_DIREITA", raio, angulo);
        buffer.inserirElemento(c);
    }

    public void CurvarEsquerda(int raio, int angulo) {
        Comando c = new Comando("CURVA_ESQUERDA", raio, angulo);
        buffer.inserirElemento(c);
    }

    /**
     * Parar:
     * - if forcar == false: insere comando PARAR no buffer
     * - if forcar == true: drena o buffer, limpa e pausa o loop
     */
    public void Parar(boolean forcar) {
        if (forcar) {
            new Thread(() -> {
                try {
                    System.out.println("[Servidor] Iniciando paragem forçada...");
                    ativa = false; // pausa o loop principal

                    // executa todos os comandos pendentes
                    while (!buffer.estaVazio()) {
                        Comando c = buffer.removerElemento();
                        if (c != null) {
                            System.out.println("[Servidor|ParagemForcada] Executando: " 
                                               + c.getTipo() + " " + c.getArg1() + " " + c.getArg2());
                            executarComandoNoRobot(c);
                        }
                    }

                    // limpa buffer com segurança
                    buffer.limpar();

                    System.out.println("[Servidor] PARAGEM FORÇADA: buffer executado e limpo!");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            buffer.inserirElemento(new Comando("PARAR", 0, 0));
        }
    }

    /* ----- Abrir / Fechar ligação ----- */

    public synchronized boolean OpenEV3(String nomeRobot) {
        boolean ok = robot.OpenEV3(nomeRobot);
        if (ok) {
            db.setRobotAberto(true);

            // start() da Thread só pode ser chamado uma vez
            if (!started) {
                this.start();  
                started = true;
                System.out.println("[Servidor] thread iniciada.");
            }

            // garante que loop está ativo
            ativa = true;

        } else {
            db.setRobotAberto(false);
        }
        return ok;
    }

    public synchronized void CloseEV3() {
        System.out.println("[Servidor] Fechando ligação ao robot...");
        ativa = false;
        robot.CloseEV3();
        db.setRobotAberto(false);
        this.interrupt(); // acorda se estiver bloqueado
    }

    /* ----- Loop de consumo ----- */

    @Override
    public void run() {
        System.out.println("[Servidor] run() iniciado. Aguardando comandos...");

        while (true) { // loop infinito
            try {
                if (!ativa) {
                    Thread.sleep(100); // pausa curta sem consumir CPU
                    continue;
                }

                Comando c = buffer.removerElemento(); // bloqueia até existir comando
                if (c != null) {
                    System.out.println("[Servidor] Executando comando: " + c.getTipo() 
                                       + " " + c.getArg1() + " " + c.getArg2());
                    executarComandoNoRobot(c);
                }

                Thread.sleep(50); // pequena pausa entre comandos

            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* ----- envia comando pro robot ----- */

    private void executarComandoNoRobot(Comando c) {
        if (c == null) return;

        switch (c.getTipo()) {
            case "RETA":
                robot.Reta(c.getArg1());
                break;
            case "CURVA_DIREITA":
                robot.CurvarDireita(c.getArg1(), c.getArg2());
                break;
            case "CURVA_ESQUERDA":
                robot.CurvarEsquerda(c.getArg1(), c.getArg2());
                break;
            case "PARAR":
                robot.Parar(false);
                break;
            default:
                System.out.println("[Servidor] Comando desconhecido: " + c.getTipo());
                break;
        }
    }

    /* ----- Getters / Setters ----- */

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }
}
