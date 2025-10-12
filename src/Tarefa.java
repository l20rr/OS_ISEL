public abstract class Tarefa extends Thread {
    protected boolean ativa = true;

    public void terminar() {
        ativa = false;
    }

    public boolean isAtiva() {
        return ativa;
    }

    @Override
    public abstract void run();
}