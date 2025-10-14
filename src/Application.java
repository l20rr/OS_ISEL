public class Application {
    private GUI gui;
    private BaseDados db;
    private BufferCircular buffer;
    private Servidor servidor;

    public Application() {
    
        db = new BaseDados();
        buffer = new BufferCircular();
        db.setBuffer(buffer); 
        // Cria e inicia o consumidor
        servidor = new Servidor(buffer, db);
        servidor.start();

        // Cria GUI
        gui = new GUI(this);
        gui.setVisible(true);
    }

    public BaseDados getDB() {
        return db;
    }

    public void run() {
        System.out.println("A aplicação começou!!");

        while (!db.isTerminar()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // finalização
        servidor.terminar();
        System.out.println("A aplicação terminou!");
    }

    public static void main(String[] args) {
        Application app = new Application();
        app.run();
    }
}
