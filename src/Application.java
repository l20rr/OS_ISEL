public class Application {
    private GUI gui;
    private BaseDados db;

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
        while (!db.isTerminar()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.print("A aplicação terminou!");
    }

    public static void main(String[] args) {
        Application app = new Application();
        app.run();
    }
}