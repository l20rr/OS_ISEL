
import java.util.concurrent.Semaphore;

public abstract class Tarefa extends Thread{
	private final byte BLOQUEADO = 0, EXECUCAO = 1, DORMIR = 2;
	
	private Semaphore sem;
	private byte estado;
	
	public Tarefa() {
		estado = BLOQUEADO;
		sem = new Semaphore(0);
	}
	
	public void desbloquear() {
		estado = EXECUCAO;
		sem.release();
	}
	
	
	public void bloquear() {
		estado = BLOQUEADO;
		try {
			sem.acquire();
		} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	private void esperaTrabalho() {
		try {
			sem.acquire();
		} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	protected abstract void runing();
	
	protected void dormir() {
		try {
			Thread.sleep((long) Math.random() * 1000);
		} catch (InterruptedException e) {e.printStackTrace();}
		
	}
	
	public void run() {
		while (true) {
			switch (estado) {
			case BLOQUEADO:
				esperaTrabalho();
				break;
			case EXECUCAO:
				runing();
				if (estado == EXECUCAO)
					estado = DORMIR;
				break;
			case DORMIR:
				dormir();
				if (estado == DORMIR)
					estado = EXECUCAO;
				break;
			}
		}
	}

}
