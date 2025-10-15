import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * BufferCircular — comunicação thread-safe entre produtores (ex: GUI, MovimentosAleatorios)
 * e o consumidor (Servidor). Usa semáforos para controle e timeouts para evitar bloqueio.
 */
public class BufferCircular {

    private final int dimensaoBuffer = 4;
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


    public boolean inserirElemento(Comando c) {
        try {
            if (!elementosLivres.tryAcquire(500, TimeUnit.MILLISECONDS)) {
                System.out.println("[Buffer] Cheio — comando descartado: " + c.getTipo());
                return false; // não conseguiu inserir a tempo
            }

            acessoElemento.acquire();
            bufferCircular[putBuffer] = c;
            putBuffer = (putBuffer + 1) % dimensaoBuffer;
            acessoElemento.release();

            elementosOcupados.release();
            System.out.println("[Buffer] Inserido comando: " + c.getTipo());
            return true;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

 
    public Comando removerElemento() {
        try {
            if (!elementosOcupados.tryAcquire(500, TimeUnit.MILLISECONDS)) {
                // Timeout sem comando disponível
                return null;
            }

            acessoElemento.acquire();
            Comando c = bufferCircular[getBuffer];
            bufferCircular[getBuffer] = null;
            getBuffer = (getBuffer + 1) % dimensaoBuffer;
            acessoElemento.release();

            elementosLivres.release();
            return c;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    /**
     * Limpa o buffer de maneira segura (sem recriar semáforos).
     */
    public void limpar() {
        try {
            acessoElemento.acquire();

            for (int i = 0; i < dimensaoBuffer; i++) {
                bufferCircular[i] = null;
            }

            putBuffer = 0;
            getBuffer = 0;

            // Reinicializa contadores sem destruir semáforos
            elementosLivres.drainPermits();
            elementosOcupados.drainPermits();

            elementosLivres.release(dimensaoBuffer);

            acessoElemento.release();
            System.out.println("[Buffer] Limpo e reinicializado.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Verifica se o buffer está vazio.
     */
    public boolean estaVazio() {
        return elementosOcupados.availablePermits() == 0;
    }

    /**
     * Verifica se o buffer está cheio.
     */
    public boolean estaCheio() {
        return elementosLivres.availablePermits() == 0;
    }

    /**
     * Retorna o tamanho atual (quantos comandos estão no buffer).
     */
    public int tamanhoAtual() {
        return elementosOcupados.availablePermits();
    }
}
