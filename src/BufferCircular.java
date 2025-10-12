import java.util.concurrent.Semaphore;

public class BufferCircular {
    private final int dimensaoBuffer = 8;
    private Comando[] bufferCircular;
    private int putBuffer, getBuffer;
    private Semaphore elementosLivres, elementosOcupados, acessoElemento;

    public BufferCircular() {
        bufferCircular = new Comando[dimensaoBuffer];
        putBuffer = 0;
        getBuffer = 0;
        elementosLivres = new Semaphore(dimensaoBuffer);
        elementosOcupados = new Semaphore(0);
        acessoElemento = new Semaphore(1);
    }

    // INSERE comando no buffer
    public void inserirElemento(Comando c) {
        try {
            elementosLivres.acquire();     
            acessoElemento.acquire();      // bloqueia acesso simultâneo
            bufferCircular[putBuffer] = c; // insere o comando
            putBuffer = (putBuffer + 1) % dimensaoBuffer; 
            acessoElemento.release();
            elementosOcupados.release();   // sinaliza que há um comando a mais
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // REMOVE comando do buffer
    public Comando removerElemento() {
        Comando c = null;
        try {
            elementosOcupados.acquire();   
            acessoElemento.acquire();      // bloqueia acesso simultâneo
            c = bufferCircular[getBuffer];
            getBuffer = (getBuffer + 1) % dimensaoBuffer;
            acessoElemento.release();
            elementosLivres.release();     // libera espaço
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return c;
    }
}
