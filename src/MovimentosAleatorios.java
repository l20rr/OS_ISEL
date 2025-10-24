import java.util.Random;
import java.util.concurrent.Semaphore;

public class MovimentosAleatorios extends Tarefa {
    private final BaseDados db;
    private final int quantidadeComandos;
    private final Random rand;
    private final GUI gui;

    private Comando myComando;
    private final Semaphore livreMyComando = new Semaphore(1);
    private final Semaphore ocupadoMyComando = new Semaphore(0);

    private static final double VELOCIDADE_CM_S = 20.0;
    private static final int TEMPO_COMUNICACAO_MS = 100;

    public MovimentosAleatorios(BaseDados db, int quantidadeComandos, GUI gui) {
        this.db = db;
        this.gui = gui;
        this.quantidadeComandos = Math.max(1, quantidadeComandos);
        this.rand = new Random();
        this.ativa = true;
    }
    
    public static String formatLinha(int index, Comando c) {
        String nome;
        switch (c.getTipo()) {
            case "RETA" -> nome = "Reta";
            case "CURVA_DIREITA" -> nome = "Curva_Direita";
            case "CURVA_ESQUERDA" -> nome = "Curva_Esquerda";
            default -> nome = c.getTipo();
        }
        return index + " - " + nome + " (" + c.getArg1() + "," + c.getArg2() + ")";
    }

    private void gerarComando() {
        int tipo = rand.nextInt(3);
        Comando c;
        switch (tipo) {
            case 0 -> c = new Comando("RETA", 10 + rand.nextInt(41), 0);
            case 1 -> c = new Comando("CURVA_DIREITA", 10 + rand.nextInt(21), 20 + rand.nextInt(71));
            default -> c = new Comando("CURVA_ESQUERDA", 10 + rand.nextInt(21), 20 + rand.nextInt(71));
        }

        try {
            livreMyComando.acquire();
            myComando = c;
            System.out.println("[MovimentosAleatorios] Gerado comando: " + c);
            ocupadoMyComando.release();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public Comando obterComando() {
        Comando c = null;
        try {
            if (!ocupadoMyComando.tryAcquire()) return null; // não bloqueia
            c = myComando;
            livreMyComando.release();
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
        return c;
    }

    /** Para a thread de forma segura */
    public void pararMovimentos() {
        this.ativa = false;

        // libera semáforos para não travar em acquires
        livreMyComando.release();
        ocupadoMyComando.release();

        System.out.println("[MovimentosAleatorios] Movimentos aleatórios parados.");
    }

    @Override
    public void run() {
        System.out.println("[MovimentosAleatorios] Thread iniciada.");

        while (isAtiva()) {
            if (db.getServidor() == null || !db.getServidor().isAtiva()) {
                try { Thread.sleep(500); } catch (InterruptedException e) { break; }
                continue;
            }

            for (int i = 0; i < quantidadeComandos && isAtiva(); i++) {
                gerarComando();
                Comando c = obterComando();
                if (c != null) db.getServidor().buffer.inserirElemento(c);
                if (c != null && gui != null) {
                    gui.MyPrint(formatLinha(i + 1, c));
                }

                try { Thread.sleep(50); } catch (InterruptedException e) { return; }
            }
            
            Comando parar  = new Comando("PARAR",0,0);
            db.getServidor().buffer.inserirElemento(parar);
            if(gui != null) gui.MyPrint("Parar(false)");

            try { Thread.sleep(500); } catch (InterruptedException e) { break; }
        }

        System.out.println("[MovimentosAleatorios] Thread finalizada.");
    }
}
