import java.util.concurrent.Semaphore;

public abstract class Tarefa extends Thread {

    private enum Estado {
        BLOQUEADO, EXECUTAR
    }

    private final Semaphore semaforo = new Semaphore(0);
    private volatile Estado estado = Estado.BLOQUEADO;
    private volatile boolean ativo = true;

    public void desbloquear() {
        estado = Estado.EXECUTAR;
        semaforo.release();
    }

    public void bloquear() {
        estado = Estado.BLOQUEADO;
        semaforo.drainPermits();
    }

    public void terminar() {
        ativo = false;
        desbloquear();
    }

    protected abstract void executar();

    @Override
    public void run() {
        while (ativo && !isInterrupted()) {
            try {
                semaforo.acquire();
                if (!ativo) break;

                while (estado == Estado.EXECUTAR && ativo) {
                    executar();
                    Thread.sleep(20); // cooperação entre threads
                }

            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }
}
