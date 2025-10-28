/* ===================== Servidor ===================== */
import java.util.Objects;

public class Servidor extends Tarefa {
    public final BufferCircular buffer;
    private final BaseDados db;
    private final RobotLegoEV3Simula robot;

    private volatile boolean ativa = false;
    private volatile boolean started = false;

    public Servidor(BufferCircular buffer, BaseDados db) {
        this.buffer = Objects.requireNonNull(buffer);
        this.db = Objects.requireNonNull(db);
        this.robot = new RobotLegoEV3Simula();
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
            new Thread(() -> {
                try {
                  
                    setAtiva(false); // pausa o loop principal

                    // executa todos os comandos pendentes
                    while (!buffer.estaVazio()) {
                        Comando c = buffer.removerElemento(); // já bloqueia corretamente com semáforos
                        if (c != null) {
                           
                            executarComandoNoRobot(c);
                        }
                    }

                    buffer.limpar(); // limpa buffer sem quebrar semáforos
                

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            buffer.inserirElemento(new Comando("PARAR", 0, 0));
        }
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public synchronized boolean OpenEV3(String nomeRobot) {
        boolean ok = robot.OpenEV3(nomeRobot);
        if (ok) {
            db.setRobotAberto(true);
            if (!started) { this.start(); started = true; }
            ativa = true;
        } else db.setRobotAberto(false);
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
                	Thread.sleep(100); continue; 
                	}
                Comando c = buffer.removerElemento();
                if (c != null) executarComandoNoRobot(c);
               
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }

    private void executarComandoNoRobot(Comando c) {
        if (c == null) return;
        switch (c.getTipo()) {
            case "RETA": 
            	robot.Reta(c.getArg1());
            
            case "CURVA_DIREITA" : 
            	robot.CurvarDireita(c.getArg1(), c.getArg2());
            case "CURVA_ESQUERDA": 
            	robot.CurvarEsquerda(c.getArg1(), c.getArg2());
            case "PARAR": 
            	robot.Parar(false);
        }
    }

  
}
