public class Application {

    private GUI gui;
    private GUI2 gui2;
    private BaseDados db;
    private Gravador gravador;
    private EvitarObstaculo evitar;

    public Application() {
        // Inicializar BaseDados
        db = new BaseDados();

        // Inicializar Gravador
        gravador = db.getGravador();

        // Inicializar EvitarObstaculo com Gravador
        evitar = new EvitarObstaculo(db, gravador);

        // Inicializar GUIs
        gui = new GUI(this);
        gui2 = new GUI2(this);

        // Mostrar GUIs
        gui.setVisible(true);
        gui2.setVisible(true);
    }

    public BaseDados getDB() { return db; }

    public static void main(String[] args) {
        new Application().run();
    }

    public void run() {
        System.out.println("A aplicação começou!");
        while (!db.isTerminar()) {
            try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
        }
        db.getServidor().interrupt();
        System.out.println("A aplicação terminou!");
    }
}
