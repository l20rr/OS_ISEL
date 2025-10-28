import java.util.concurrent.Semaphore;
import java.util.Random;

/* ===================== Buffer Circular ===================== */
public class BufferCircular {
    private final int dimensaoBuffer = 16;
    private final Comando[] bufferCircular;
    private int putBuffer, getBuffer;

    private final Semaphore elementosLivres;
    private final Semaphore elementosOcupados;
    private final Semaphore acessoElemento;

    public BufferCircular() {
        bufferCircular = new Comando[dimensaoBuffer];
        putBuffer = 0;
        getBuffer = 0;
        elementosLivres = new Semaphore(dimensaoBuffer);
        elementosOcupados = new Semaphore(0);
        acessoElemento = new Semaphore(1);
    }

    public void inserirElemento(Comando c) {
        try {
            elementosLivres.acquire();
            acessoElemento.acquire();
            bufferCircular[putBuffer] = c;
            putBuffer = (putBuffer + 1) % dimensaoBuffer;
            acessoElemento.release();
            elementosOcupados.release();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public Comando removerElemento() {
        Comando c = null;
        try {
            elementosOcupados.acquire();
            acessoElemento.acquire();
            c = bufferCircular[getBuffer];
            bufferCircular[getBuffer] = null;
            getBuffer = (getBuffer + 1) % dimensaoBuffer;
            acessoElemento.release();
            elementosLivres.release();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return c;
    }

    public boolean estaVazio() {
        return elementosOcupados.availablePermits() == 0;
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
