/*******
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
        this.gui = gui;
        this.quantidadeComandos = Math.max(1, quantidadeComandos);
        this.rand = new Random();
        this.ativa = true;
    }

    public static String formatLinha(int index, Comando c) {
        String nome;
        switch (c.getTipo()) {
            case "RETA":
                nome = "Reta";
                break;
            case "CURVA_DIREITA":
                nome = "Curva_Direita";
                break;
            case "CURVA_ESQUERDA":
                nome = "Curva_Esquerda";
                break;
            default:
                nome = c.getTipo();
                break;
        }
        return index + " - " + nome + " (" + c.getArg1() + "," + c.getArg2() + ")";
    }

    private Comando gerarComando() {
        int tipo = rand.nextInt(3);
        Comando c = null;

        switch (tipo) {
            case 0:
                int distancia = 10 + rand.nextInt(41);
                c = new Comando("RETA", distancia, 0);
                break;

            case 1:
                int raio_D = 10 + rand.nextInt(21);
                int angulo_D = 20 + rand.nextInt(71);
                c = new Comando("CURVA_DIREITA", raio_D, angulo_D);
                break;

            case 2:
                int raio_E = 10 + rand.nextInt(21);
                int angulo_E = 20 + rand.nextInt(71);
                c = new Comando("CURVA_ESQUERDA", raio_E, angulo_E);
                break;
        }
        return c;
    }
    
    public void pararMovimentos() {
        this.ativa = false;

    }


    @Override
    public void run() {
        System.out.println("[MovimentosAleatorios] Thread iniciada.");

        while (isAtiva()) {
            if (db.getServidor() == null || !db.getServidor().isAtiva()) {
                try { 
                	Thread.sleep(500); 
                	//Espera o Servidor estar pronto antes de gerar comandos
                	} catch (InterruptedException e) { break; }
                continue;
            }

            for (int i = 0; i < quantidadeComandos && isAtiva(); i++) {
                Comando c = gerarComando();
                db.getServidor().buffer.inserirElemento(c);

                if (gui != null) gui.MyPrint(formatLinha(i + 1, c));

                try { 
                	Thread.sleep(150); 
                	//intervalo natural entre geração de comandos
                } catch (InterruptedException e) { return; }
            }

            Comando parar = new Comando("PARAR", 0, 0);
            db.getServidor().buffer.inserirElemento(parar);
            if (gui != null) gui.MyPrint("Parar(false)");

            try { Thread.sleep(50); } catch (InterruptedException e) { break; }
        }
    }
}
