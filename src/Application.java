public class Application {

    private final BaseDados db;
    private final GUI gui;
    private final GUI2 gui2;
    private final EvitarObstaculo evitar;
    private final Gravador gravador;

    public Application() {
        // 1. Criar estado global
        db = new BaseDados();
   
        // 2. Criar tarefas dependentes do estado
        evitar = new EvitarObstaculo(db, db.getGravador());
        gravador = new Gravador(db.getBufferRec());
        // 3. Criar interfaces
        gui = new GUI(this);
        gui2 = new GUI2(this);

    
    }

    public BaseDados getDB() {
        return db;
    }

    public void run() {
        System.out.println("Aplicação iniciada.");

        while (!db.isTerminar()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }

        // Encerramento controlado
        if (db.isRobotAberto()) {
            db.getServidor().pararForcado();
            db.getServidor().closeEV3();
        }

        db.getServidor().interrupt();
        //evitar.interrupt();

        System.out.println("Aplicação terminada.");
    }

    public static void main(String[] args) {
        new Application().run();
    }
}
