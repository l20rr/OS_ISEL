import java.util.Random;

public class EvitarObstaculo extends Tarefa {
    private final BaseDados db;
    private final Servidor servidor;
    private final RobotLegoEV3 robot;
    private final Random rand = new Random();
    private boolean toqueAnterior = false;

    public EvitarObstaculo(BaseDados db) {
        this.db = db;
        this.servidor = db.getServidor();
        this.robot = servidor.robot;
        this.start();
        desbloquear();
    }

  
    @Override
    protected void runing() {
        if (db.isRobotAberto() && robot != null) {
            int toqueAtual = robot.SensorToque(robot.S_1);

            if (toqueAtual == 1 && !toqueAnterior) {
                System.out.println("Obstáculo detetado!");
                Servidor servidor = db.getServidor();

                try {
                    servidor.s.acquire(); 

                    // Parar imediatamente
                    robot.Parar(true);
                    Thread.sleep(200);

                    // Recuar 20 cm
                    robot.Reta(-20);
                    Thread.sleep(servidor.tempoReta(20));

                    // Curva aleatória 90°
                    if (Math.random() < 0.5) {
                        robot.CurvarDireita(10, 90);
                        Thread.sleep(servidor.tempoCurva(10, 90));
                    } else {
                        robot.CurvarEsquerda(10, 90);
                        Thread.sleep(servidor.tempoCurva(10, 90));
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    servidor.s.release(); 
                }
            }

            toqueAnterior = (toqueAtual == 1);
        }

        try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

}
