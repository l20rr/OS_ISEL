
//Consumidor

public class Servidor extends Tarefa {
    private BufferCircular buffer;
    private BaseDados db;

    public Servidor(BufferCircular buffer, BaseDados db) {
        this.buffer = buffer;
        this.db = db;
    }

    @Override
    public void run() {
        while (isAtiva()) {
            Comando c = buffer.removerElemento();
            System.out.println("Executando comando: " + c.tipo + " " + c.arg1 + " " + c.arg2);
            if (db.isRobotAberto()) {
                switch (c.tipo) {
                    case "RETA":
                        db.getRobot().Reta(c.arg1);
                        break;
                    case "CURVA":
                        db.getRobot().CurvarDireita(c.arg1, c.arg2);
                        break;
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}