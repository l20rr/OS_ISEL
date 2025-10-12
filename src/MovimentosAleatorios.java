//Produtor

import java.util.Random;

public class MovimentosAleatorios extends Tarefa {
    private BufferCircular buffer;
    private int quantidade;
    private Random rand = new Random();

    public MovimentosAleatorios(BufferCircular buffer, int quantidade) {
        this.buffer = buffer;
        this.quantidade = quantidade;
    }

    @Override
    public void run() {
       
    	
    }
}
