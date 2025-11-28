/*/*******
 * 
 * Thread produtora
 * 
 * Gera comandos e coloca no buffer
 * 
 * *******/
import java.util.Random;

public class MovimentosAleatorios extends Tarefa {
    private final BaseDados db;
    private final int quantidadeComandos;
    private final Random rand;
    private final GUI gui;

    public MovimentosAleatorios(BaseDados db, int quantidadeComandos, GUI gui) {
        this.db = db;
        this.quantidadeComandos = Math.max(1, quantidadeComandos);
        this.rand = new Random();
        this.gui = gui;
    }

    private Comando gerarComando() {
        int tipo = rand.nextInt(3);
        switch (tipo) {
            case 0:
                return new Comando("RETA", 10 + rand.nextInt(41), 0);
            case 1:
                return new Comando("CURVA_DIREITA", 10 + rand.nextInt(21), 20 + rand.nextInt(71));
            default:
                return new Comando("CURVA_ESQUERDA", 10 + rand.nextInt(21), 20 + rand.nextInt(71));
        }
    }

    private String formatLinha(int index, Comando c) {
        String nome;
        switch (c.getTipo()) {
            case "RETA": nome = "Reta"; break;
            case "CURVA_DIREITA": nome = "Curva Direita"; break;
            case "CURVA_ESQUERDA": nome = "Curva Esquerda"; break;
            default: nome = c.getTipo(); break;
        }
        return index + " - " + nome + " (" + c.getArg1() + "," + c.getArg2() + ")";
    }

    @Override
    protected void runing() {
        if (db.getServidor() == null) {
            try { Thread.sleep(500); } catch (InterruptedException e) { return; }
            return;
        }

        try {
            // Bloqueia o semáforo uma única vez para o lote todo
            db.getServidor().s.acquire();

            for (int i = 0; i < quantidadeComandos; i++) {
            	if(Thread.currentThread().isInterrupted()) {
            		return;
            	}
                Comando c = gerarComando();
                db.getServidor().buffer.inserirElemento(c);
                db.getGravador().registarComando(c);
                if (gui != null) gui.MyPrint(formatLinha(i + 1, c));

                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            // comando final: PARAR
            if(!Thread.currentThread().isInterrupted()) {
            	Comando parar = new Comando("PARAR", 0, 0);
            	db.getServidor().buffer.inserirElemento(parar);
                if (gui != null) gui.MyPrint("Parar(false)");
        	}

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
          
            db.getServidor().s.release();
        }
    }

}