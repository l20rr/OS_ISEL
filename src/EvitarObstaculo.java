import java.util.Random;

public class EvitarObstaculo extends Tarefa {
    private final BaseDados db;
    private final Servidor servidor;
    private final RobotLegoEV3Simula robot;
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
    	
        synchronized(robot) {
        	if (db.isRobotAberto() && robot != null) {
                int toqueAtual = robot.SensorToque(robot.S_1);

                if (toqueAtual == 1 && !toqueAnterior) {
                    System.out.println("Obstáculo detetado!");
                    Servidor servidor = db.getServidor();

                    
                    	// Parar imediatamente
                        robot.Parar(true);
                        try {
    						Thread.sleep(10);
    					} catch (InterruptedException e) {
    						
    						e.printStackTrace();
    					}

                        // Recuar 20 cm
                        robot.Reta(-20);
                        robot.Parar(false);
                        try {
    						Thread.sleep(servidor.tempoReta(20));
    					} catch (InterruptedException e) {
    						
    						e.printStackTrace();
    					}

                        // Curva aleatória 90°
                        if (Math.random() < 0.5) {
                            robot.CurvarDireita(10, 90);
                            robot.Parar(false);
                            try {
    							Thread.sleep(servidor.tempoCurva(10, 90));
    						} catch (InterruptedException e) {
    							
    							e.printStackTrace();
    						}
                        } else {
                            robot.CurvarEsquerda(10, 90);
                            robot.Parar(false);
                            try {
    							Thread.sleep(servidor.tempoCurva(10, 90));
    						} catch (InterruptedException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
                        }
                    

                        

                    
                }

                toqueAnterior = (toqueAtual == 1);
            }

            try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }

}