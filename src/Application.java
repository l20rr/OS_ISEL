public class Application {
    private GUI gui; 
    
    public Application() {
        gui = new GUI();
        gui.setVisible(true); // <- isto faz aparecer a janela
    }
    
    public void run() {
        System.out.print("A app começou!!");
        while(!gui.getDB().isTerminar()) {
            try {
                Thread.sleep(100);
                
            }catch(InterruptedException e) {
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
