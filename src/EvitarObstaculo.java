import java.util.Random;

public class EvitarObstaculo extends Tarefa {
    private final BaseDados db;
    private final Servidor servidor;
    private final RobotLegoEV3Simula robot;
    private final Gravador gravador;
    private final Random rand = new Random();

    public EvitarObstaculo(BaseDados db, Gravador gravador) {
        this.db = db;
        this.servidor = db.getServidor();
        this.robot = servidor.robot;
        this.gravador = gravador;
        start();
        desbloquear();
    }

    @Override
	protected void executar() {
        synchronized(robot) {
            if (db.isRobotAberto() && robot != null) {
                int toqueAtual = robot.SensorToque(robot.S_1);

                if (toqueAtual == 1 ) {
                    System.out.println("Obstáculo detetado!");

                    // Parar imediatamente
                    robot.Parar(true);
                    if (gravador.isGravando()) gravador.registarComando(new Comando("PARAR", 0, 0));

                    try { Thread.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }

                    // Recuar 20 cm
                    robot.Reta(-20);
                    if (gravador.isGravando()) gravador.registarComando(new Comando("TRAS", 20, 0));

                    robot.Parar(false);
                    try { Thread.sleep(servidor.tempoReta(20)); } catch (InterruptedException e) { e.printStackTrace(); }

                    // Curva aleatória 90°
                    if (rand.nextBoolean()) {
                        robot.CurvarDireita(10, 90);
                        if (gravador.isGravando()) gravador.registarComando(new Comando("DIREITA", 10, 90));
                        robot.Parar(false);
                        try { Thread.sleep(servidor.tempoCurva(10, 90)); } catch (InterruptedException e) { e.printStackTrace(); }
                    } else {
                        robot.CurvarEsquerda(10, 90);
                        if (gravador.isGravando()) gravador.registarComando(new Comando("ESQUERDA", 10, 90));
                        robot.Parar(false);
                        try { Thread.sleep(servidor.tempoCurva(10, 90)); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                }
            }

            try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }


}
