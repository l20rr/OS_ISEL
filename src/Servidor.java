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
    private final RobotLegoEV3Simula robot;
    public final Semaphore s = new Semaphore(1); //sincronização com a gui

    public Servidor(BufferCircular buffer, BaseDados db) {
        this.buffer = Objects.requireNonNull(buffer);
        this.db = Objects.requireNonNull(db);
        this.robot = new RobotLegoEV3Simula();
        this.start();
    }

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

    private void executarComandoNoRobot(Comando c) {
        if (c == null) return;

        switch (c.getTipo()) {
            case "RETA":
                robot.Reta(c.getArg1());
                sleepTempo(CalcularTempos.tempoReta(c.getArg1()));
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
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
