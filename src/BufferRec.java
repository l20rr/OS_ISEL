import java.util.concurrent.Semaphore;

/* ===================== Buffer para Gravador ===================== */
public class BufferRec {
    private final int dimensaoBuffer = 16; // mesmo tamanho do buffer circular
    private final Comando[] bufferRec;
    private int putBuffer, getBuffer;

    private final Semaphore elementosLivres;
    private final Semaphore elementosOcupados;
    private final Semaphore acessoElemento;

    public BufferRec() {
        bufferRec = new Comando[dimensaoBuffer];
        putBuffer = 0;
        getBuffer = 0;
        elementosLivres = new Semaphore(dimensaoBuffer);
        elementosOcupados = new Semaphore(0);
        acessoElemento = new Semaphore(1);
    }

    // Inserir comando no buffer do gravador
    public void inserirElemento(Comando c) {
        try {
            elementosLivres.acquire(); // espera espaço livre
            acessoElemento.acquire();  // acesso exclusivo ao buffer
            bufferRec[putBuffer] = c;
            putBuffer = (putBuffer + 1) % dimensaoBuffer;
            acessoElemento.release();
            elementosOcupados.release(); // sinaliza comando disponível
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Remover comando do buffer do gravador
    public Comando removerElemento() {
        Comando c = null;
        try {
            elementosOcupados.acquire(); // espera comando disponível
            acessoElemento.acquire();
            c = bufferRec[getBuffer];
            bufferRec[getBuffer] = null;
            getBuffer = (getBuffer + 1) % dimensaoBuffer;
            acessoElemento.release();
            elementosLivres.release(); // sinaliza espaço livre
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return c;
    }


    public void limpar() {
        try {
            elementosOcupados.drainPermits(); 
            acessoElemento.acquire();
            putBuffer = getBuffer; 
            elementosLivres.release(dimensaoBuffer - elementosLivres.availablePermits());
            acessoElemento.release();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
