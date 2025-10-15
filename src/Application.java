public class Application {
    private GUI gui;
    private BaseDados db;

    public Application() {
        db = new BaseDados(); // BaseDados já cria o buffer e servidor
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

        // Finalização
        db.getServidor().terminar();
        System.out.println("A aplicação terminou!");
    }

    public static void main(String[] args) {
        Application app = new Application();
        app.run();
    }
}
