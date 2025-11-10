/*****Thread consumidor
 * 
 * Executa comandos do buffer e simula o tempo real de movimento
 * 
 * *****/

import java.util.Objects;
import java.util.concurrent.Semaphore;

public class Servidor extends Tarefa {
    public final BufferCircular buffer;
    private final BaseDados db;
    public final RobotLegoEV3 robot;
    public final Semaphore s = new Semaphore(1); //sincronização com a gui

    public Servidor(BufferCircular buffer, BaseDados db) {
        this.buffer = Objects.requireNonNull(buffer);
        this.db = Objects.requireNonNull(db);
        this.robot = new RobotLegoEV3();
        this.start();
    }


    private final double vel = 0.02;
    private final int comunicacao = 100;
    
    
    public void Reta(int distancia) { buffer.inserirElemento(new Comando("RETA", distancia, 0)); }
    public void CurvarDireita(int raio, int angulo) { buffer.inserirElemento(new Comando("CURVA_DIREITA", raio, angulo)); }
    public void CurvarEsquerda(int raio, int angulo) { buffer.inserirElemento(new Comando("CURVA_ESQUERDA", raio, angulo)); }

    public void Parar(boolean forcar) {
        if (forcar) {
            System.out.println("[Servidor] Parada forçada — limpando buffer e parando robô.");
            buffer.limpar();         // esvazia todos os comandos pendentes
            robot.Parar(true);       // para imediatamente o robô
           
        } else {
            buffer.inserirElemento(new Comando("PARAR", 0, 0));
        }
    }

    public synchronized boolean OpenEV3(String nomeRobot) {
        boolean ok = robot.OpenEV3(nomeRobot);
        if (ok) {
            db.setRobotAberto(true);
            desbloquear(); // acorda a thread se estava bloqueada
            
        } else {
            db.setRobotAberto(false);
        }
        return ok;
    }

    public synchronized void CloseEV3() {
        robot.CloseEV3();
        db.setRobotAberto(false);
        bloquear(); // pausa a thread até reabrir
    }

    @Override
    protected void runing() {
        try {
            Comando c = buffer.removerElemento();
            if (c != null) executarComandoNoRobot(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public int tempoReta(int d) {
        // d está em cm, vel em cm/ms → resultado em ms
        return (int) ((d / vel) + comunicacao);
    }

    public int tempoCurva(int r, int a) {
        double rad = 2 * Math.PI * r * (a / 360.0);
        return (int) ((rad / vel) + comunicacao);
    }

    public int tempoParar() {
        return comunicacao;
    }


    private void executarComandoNoRobot(Comando c) {
        if (c == null) return;
        try {
            s.acquire(); 

            switch (c.getTipo()) {
                case "RETA":
                    robot.Reta(c.getArg1());
                    sleepTempo(tempoReta(c.getArg1()));
                    break;
                case "CURVA_DIREITA":
                    robot.CurvarDireita(c.getArg1(), c.getArg2());
                    sleepTempo(tempoCurva(c.getArg1(), c.getArg2()));
                    break;
                case "CURVA_ESQUERDA":
                    robot.CurvarEsquerda(c.getArg1(), c.getArg2());
                    sleepTempo(tempoCurva(c.getArg1(), c.getArg2()));
                    break;
                case "PARAR":
                    robot.Parar(false);
                    sleepTempo(tempoParar());
                    break;
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            s.release(); 
        }
    }

    


    
    
    private void sleepTempo(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
