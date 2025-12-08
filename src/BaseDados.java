public class BaseDados {

    private boolean terminar; 
    private boolean robotAberto;
    private String nomeRobot;

    private final BufferCircular buffer;
    private final BufferRec bufferRec;
    private final Servidor servidor; 
    private final Gravador gravador;

    private int ultimaDistancia; 
    private int ultimoAngulo; 
    private int ultimoRaio; 

    public BaseDados() {
        this.terminar = false;
        this.robotAberto = false;
        this.nomeRobot = "EV7";

        this.buffer = new BufferCircular();
        this.bufferRec = new BufferRec();
        this.servidor = new Servidor(buffer, this);
        this.gravador = new Gravador(bufferRec);

        this.ultimaDistancia = 20;
        this.ultimoAngulo = 90;
        this.ultimoRaio = 10;
    }

    public Servidor getServidor() { return servidor; }
    public BufferCircular getBuffer() { return buffer; }
    public BufferRec getBufferRec() { return bufferRec; }
    public boolean isRobotAberto() { return robotAberto; }
    public void setRobotAberto(boolean robotAberto) { this.robotAberto = robotAberto; }
    public boolean isTerminar() { return terminar; }
    public void setTerminar(boolean terminar) { this.terminar = terminar; }
    public String getNomeRobot() { return nomeRobot; }
    public void setNomeRobot(String nomeRobot) { this.nomeRobot = nomeRobot; }
    public int getUltimaDistancia() { return ultimaDistancia; }
    public void setUltimaDistancia(int ultimaDistancia) { this.ultimaDistancia = ultimaDistancia; }
    public int getUltimoAngulo() { return ultimoAngulo; }
    public void setUltimoAngulo(int ultimoAngulo) { this.ultimoAngulo = ultimoAngulo; }
    public int getUltimoRaio() { return ultimoRaio; }
    public void setUltimoRaio(int ultimoRaio) { this.ultimoRaio = ultimoRaio; }
    public Gravador getGravador() { return gravador; }
}
