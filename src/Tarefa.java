public abstract class Tarefa extends Thread {
	
    protected boolean ativa = true;
    
    //desbloquear , bloquear , esperaTarefa o professor disse que tinhamos que ter isto
    /*public void bloquear() {
    	estado = BLOQUEADO;
    	try {
    		sem.acquire();
    	}catch (InterruptExceptione) {e.printStackTrace();}
    }*/
    
    
    public void terminar() {
        ativa = false;
    }

    public boolean isAtiva() {
        return ativa;
    }

    @Override
    public abstract void run();
}