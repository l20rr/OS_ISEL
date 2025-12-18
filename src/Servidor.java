import java.util.Objects;
import java.util.concurrent.Semaphore;

public class Servidor extends Tarefa {

    private final BaseDados db;
    final RobotLegoEV3Simula robot;
    private final BufferCircular buffer;
    private final Semaphore semBloco = new Semaphore(1, true);

    private final double vel = 0.02; // cm/ms
    private final int comunicacao = 100;
	
    
    public Servidor(BufferCircular buffer, BaseDados db) {
        this.buffer = Objects.requireNonNull(buffer);
        this.db = Objects.requireNonNull(db);
        this.robot = new RobotLegoEV3Simula();
        start();
    }
    
    public RobotLegoEV3Simula getRobot() {
        return robot;
    }


    // ==== Controle do robô ====
    public synchronized boolean openEV3(String nomeRobot) {
        boolean ok = robot.OpenEV3(nomeRobot);
        db.setRobotAberto(ok);
        if (ok) desbloquear(); // libera execução
        else bloquear();
        return ok;
    }
    
    public void pararForcado() {
    	 semBloco.drainPermits(); // mata qualquer bloco
    	    semBloco.release();     // reabre sistema

    	    buffer.limpar();
    	    robot.Parar(true);
        //gravador.registarComando(c);
    }

    @Override
    protected void executar() {
        try {
            Comando comando = buffer.removerElemento();
            if (comando != null) {
                executarNoRobot(comando);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void inserirComando(Comando c) {
        buffer.inserirElemento(c);
        db.getGravador().registarComando(c);
    }

    public void iniciarBloco() throws InterruptedException {
        semBloco.acquire(); // 🔒 bloqueia outros produtores
    }

    public void terminarBloco() {
        semBloco.release(); // 🔓 libera o sistema
    }
    
    public void inserirComandoAposBloco(Comando c) throws InterruptedException {
        semBloco.acquire();      // ⏳ espera bloco terminar
        try {
            inserirComando(c);   // entra em ordem
        } finally {
            semBloco.release();  // libera
        }
    }


    public synchronized void closeEV3() {
        robot.CloseEV3();
        db.setRobotAberto(false);
        bloquear(); // pausa execução até reabrir
    }

    public void parar(boolean forcar) {
        if (forcar) {
            buffer.limpar();
            robot.Parar(true);
        } else {
            buffer.inserirElemento(new Comando("PARAR", 0, 0));
        }
    }

  

    // ==== Encapsula lógica de execução ====
    private void executarNoRobot(Comando c) {
        switch (c.getTipo()) {
            case "RETA":
                synchronized (robot) {
                    robot.Reta(c.getArg1());
                }
                sleepTempo(tempoReta(c.getArg1()));
                break;
            case "CURVA_DIREITA":
                synchronized (robot) {
                    robot.CurvarDireita(c.getArg1(), c.getArg2());
                }
                sleepTempo(tempoCurva(c.getArg1(), c.getArg2()));
                break;
            case "CURVA_ESQUERDA":
                synchronized (robot) {
                    robot.CurvarEsquerda(c.getArg1(), c.getArg2());
                }
                sleepTempo(tempoCurva(c.getArg1(), c.getArg2()));
                break;
            case "PARAR":
                synchronized (robot) {
                    robot.Parar(false);
                }
                sleepTempo(tempoParar());
                break;
            default:
                System.out.println("[Servidor] Comando desconhecido: " + c.getTipo());
        }
    }

    // ==== Tempos de execução ====
    public int tempoReta(int distancia) {
        return (int)((distancia / vel) + comunicacao);
    }

    public int tempoCurva(int raio, int angulo) {
        double comprimento = 2 * Math.PI * raio * (angulo / 360.0);
        return (int)((comprimento / vel) + comunicacao);
    }

    public int tempoParar() {
        return comunicacao;
    }

    private void sleepTempo(int ms) {
        if (ms < 0) {
            ms = -ms; // transforma negativo em positivo
        }
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
