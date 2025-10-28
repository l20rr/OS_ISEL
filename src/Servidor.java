/*****Thread consumidor
 * 
 * Executa comandos do buffer e simula o tempo real de movimento
 * 
 * 
 * 
 * 
 * *****/

import java.util.Objects;

public class Servidor extends Tarefa {
    public final BufferCircular buffer;
    private final BaseDados db;
    private final RobotLegoEV3 robot;

    private volatile boolean ativa = false;
    private volatile boolean started = false;

    public Servidor(BufferCircular buffer, BaseDados db) {
        this.buffer = Objects.requireNonNull(buffer);
        this.db = Objects.requireNonNull(db);
        this.robot = new RobotLegoEV3();
    }

    public void Reta(int distancia) {
        buffer.inserirElemento(new Comando("RETA", distancia, 0));
    }

    public void CurvarDireita(int raio, int angulo) {
        buffer.inserirElemento(new Comando("CURVA_DIREITA", raio, angulo));
    }

    public void CurvarEsquerda(int raio, int angulo) {
        buffer.inserirElemento(new Comando("CURVA_ESQUERDA", raio, angulo));
    }

    public void Parar(boolean forcar) {
        if (forcar) {
            ativa = false;
            buffer.limpar();
            robot.Parar(true);
        } else {
            buffer.inserirElemento(new Comando("PARAR", 0, 0));
        }
    }

    public boolean isAtiva() { return ativa; }
    public void setAtiva(boolean ativa) { this.ativa = ativa; }

    public synchronized boolean OpenEV3(String nomeRobot) {
        boolean ok = robot.OpenEV3(nomeRobot);
        if (ok) {
            db.setRobotAberto(true);
            if (!started) {
                this.start(); //thread iniciada 
                started = true;
            }
            ativa = true;
        } else {
            db.setRobotAberto(false);
        }
        return ok;
    }

    public synchronized void CloseEV3() {
        ativa = false;
        robot.CloseEV3();
        db.setRobotAberto(false);
        this.interrupt();
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (!ativa) {
                    Thread.sleep(100); //pausa quando está desativado
                    continue;
                }

                Comando c = buffer.removerElemento();
                if (c != null) executarComandoNoRobot(c);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void executarComandoNoRobot(Comando c) {
        if (c == null) return;

        switch (c.getTipo()) {
            case "RETA":
                robot.Reta(c.getArg1());
                sleepTempo(CalcularTempos.tempoReta(c.getArg1())); //Pausa do enunciado e criada uma nova classe
                break;

            case "CURVA_DIREITA":
                robot.CurvarDireita(c.getArg1(), c.getArg2());
                sleepTempo(CalcularTempos.tempoCurva(c.getArg1(), c.getArg2()));
                break;

            case "CURVA_ESQUERDA":
                robot.CurvarEsquerda(c.getArg1(), c.getArg2());
                sleepTempo(CalcularTempos.tempoCurva(c.getArg1(), c.getArg2()));
                break;

            case "PARAR":
                robot.Parar(false);
                sleepTempo(CalcularTempos.tempoParar());
                break;

            default:
                System.out.println("[Servidor] Comando desconhecido: " + c.getTipo());
                break;
        }
    }

    private void sleepTempo(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
