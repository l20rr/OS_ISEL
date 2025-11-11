public class Application {
    private GUI gui;
    private GUI2 gui2;
    private BaseDados db;

    public Application() {
        db = new BaseDados();
        gui = new GUI(this);
        gui2 = new GUI2(this);
        gui.setVisible(true);
        gui2.setVisible(true);   }

    public BaseDados getDB() {
        return db;
    }

    public static void main(String[] args) {
        Application app = new Application();
        app.run();
    }

    public void run() {
        System.out.println("A aplicação começou!");

        while (!db.isTerminar()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        db.getServidor().interrupt();
        System.out.println("A aplicação terminou!");
    }
}
