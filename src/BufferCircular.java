
import java.util.concurrent.Semaphore;

public class BufferCircular {
	final int dimensaoBuffer= 8;
	Comando[] bufferCircular;
	int putBuffer, getBuffer;
	Semaphore elementosLivres, acessoElemento, elementosOcupados;
	
	public BufferCircular(){
	 bufferCircular= new Comando[dimensaoBuffer];
	 putBuffer= 0;
	 getBuffer= 0;
	 elementosLivres= new Semaphore(dimensaoBuffer);
	 elementosOcupados= new Semaphore(0);
	 acessoElemento= new Semaphore(1);
	}
	
	
	public void inserirElemento(Comando c){
		try {
		 elementosLivres.acquire();
		 acessoElemento.acquire();
		 bufferCircular[putBuffer]= new Comando(c);
		 putBuffer= ++putBuffer % dimensaoBuffer;
		 acessoElemento.release();
		} catch (InterruptedException e) {}
		 elementosOcupados.release();
		}
	
	public String removerElemento() {
		 String s= null;
		 try {
		 elementosOcupados.acquire();
		 acessoElemento.acquire();
		 } catch (InterruptedException e) {}
		 s= new Comando(bufferCircular[getBuffer]);
		 getBuffer= ++getBuffer % dimensaoBuffer;
		 acessoElemento.release();
		 elementosLivres.release();
		 return s;
		}

}
