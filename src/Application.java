public class Application {
    private GUI gui;
    private BaseDados db;
    private Servidor servidor;

    public Application() {
        db = new BaseDados();
        gui = new GUI(this);
        gui.setVisible(true);
    }

    public BaseDados getDB() {
        return db;
    }

    public void run() {
        System.out.print("A app começou!!");
        
        // consumidor
        servidor = new Servidor(db.getRobot().getBuffer(), db);
        servidor.start();
        
        while (!db.isTerminar()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        
        if (servidor != null && servidor.isAtiva()) {
            servidor.terminar();
        }
        System.out.print("A aplicação terminou!");
    }

    public static void main(String[] args) {
        Application app = new Application();
        app.run();
    }
}