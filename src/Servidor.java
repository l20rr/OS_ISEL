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
            if (c != null) {
                System.out.println("Executando comando: " + c.tipo + " " + c.arg1 + " " + c.arg2);
                if (db.isRobotAberto()) {
                    switch (c.tipo) {
                        case "RETA":
                            db.getRobot().Reta(c.arg1);
                            break;
                        case "CURVA_DIREITA":
                            db.getRobot().CurvarDireita(c.arg1, c.arg2);
                            break;
                        case "CURVA_ESQUERDA":
                            db.getRobot().CurvarEsquerda(c.arg1, c.arg2);
                            break;
                        case "PARAR":
                            db.getRobot().Parar(true);
                            break;
                    }
                }
            }
            try {
                Thread.sleep(100); 
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}