import java.util.Random;

public class MovimentosAleatorios extends Tarefa {

    private final BaseDados db;
    private final int quantidadeComandos;
    private final Random rand = new Random();
    private final GUI gui;

    private int contador = 0;
    private boolean blocoAdquirido = false;

    public MovimentosAleatorios(BaseDados db, int quantidadeComandos, GUI gui) {
        this.db = db;
        this.quantidadeComandos =  quantidadeComandos;
        this.gui = gui;
    }

    @Override
    protected void executar() {

        Servidor servidor = db.getServidor();
        if (servidor == null) {
            bloquear();
            return;
        }

        try {
            // iniciar novo bloco se ainda não iniciou
            if (!blocoAdquirido) {
                servidor.iniciarBloco();
                blocoAdquirido = true;
                contador = 0;
            }

            // bloco terminou → fecha bloco e prepara o próximo
            if (contador >= quantidadeComandos) {
                servidor.inserirComando(new Comando("PARAR", 0, 0));
                if (gui != null) gui.MyPrint("Bloco terminado");

                servidor.terminarBloco(); // 🔓
                blocoAdquirido = false;

                // pausa pequena antes do próximo bloco
                Thread.sleep(200);
                return; // NÃO bloquear, continua vivo
            }

            // gera comando normal
            Comando c = gerarComando();
            servidor.inserirComando(c);

            if (gui != null) {
                gui.MyPrint(formatLinha(contador + 1, c));
            }

            contador++;
            Thread.sleep(150);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            if (blocoAdquirido) {
                servidor.terminarBloco();
            }
            bloquear();
        }
    }

    private Comando gerarComando() {
        int tipo = rand.nextInt(3);
        switch (tipo) {
            case 0: return new Comando("RETA", 10 + rand.nextInt(41), 0);
            case 1: return new Comando("CURVA_DIREITA", 10 + rand.nextInt(21), 20 + rand.nextInt(71));
            default: return new Comando("CURVA_ESQUERDA", 10 + rand.nextInt(21), 20 + rand.nextInt(71));
        }
    }

    private String formatLinha(int index, Comando c) {
        return index + " - " + c.getTipo() + " (" + c.getArg1() + "," + c.getArg2() + ")";
    }
}
