public class EvitarObstaculo extends Tarefa {
    private final BaseDados db;
    private final RobotLegoEV3Simula robot;
    private boolean toqueAnterior = false;

    public EvitarObstaculo(BaseDados db) {
        this.db = db;
        this.robot = db.getServidor().robot;
        this.start();
        desbloquear();
    }

    @Override
    protected void runing() {
        if (db.isRobotAberto() && robot != null) {
            int toqueAtual = robot.SensorToque(robot.S_1);
            if (toqueAtual == 1 && !toqueAnterior) {
                System.out.println("🔵 Tocou!");
            }
            toqueAnterior = (toqueAtual == 1);
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
