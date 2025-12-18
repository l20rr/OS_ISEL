public class BaseDados {

    // ===== Estado global =====
    private volatile boolean terminar = false;
    private volatile boolean robotAberto = false;
    private String nomeRobot = "EV7";

    // ===== Parâmetros de movimento =====
    private int ultimaDistancia = 20;
    private int ultimoAngulo = 90;
    private int ultimoRaio = 10;

    // ===== Infraestrutura =====
    private final BufferCircular buffer;
    private final BufferRec bufferRec;
    private final Servidor servidor;
    private final Gravador gravador;

    public BaseDados() {
        buffer = new BufferCircular();
        bufferRec = new BufferRec();
        gravador = new Gravador(bufferRec);
        servidor = new Servidor(buffer, this);
    }
    
    private volatile boolean evitarAtivo = false;


    // ===== Getters =====
    public Servidor getServidor() { return servidor; }
    public Gravador getGravador() { return gravador; }
    public BufferCircular getBuffer() { return buffer; }
    public BufferRec getBufferRec() { return bufferRec; }

    // ===== Estado =====
    public boolean isTerminar() { return terminar; }
    public boolean isEvitarAtivo() {
        return evitarAtivo;
    }

    public void setTerminar(boolean terminar) { this.terminar = terminar; }

    public boolean isRobotAberto() { return robotAberto; }
    public void setRobotAberto(boolean robotAberto) { this.robotAberto = robotAberto; }
    public void setEvitarAtivo(boolean evitarAtivo) {
        this.evitarAtivo = evitarAtivo;
    }

    public String getNomeRobot() { return nomeRobot; }
    public void setNomeRobot(String nomeRobot) { this.nomeRobot = nomeRobot; }

    // ===== Movimento =====
    public int getUltimaDistancia() { return ultimaDistancia; }
    public void setUltimaDistancia(int d) { ultimaDistancia = d; }

    public int getUltimoAngulo() { return ultimoAngulo; }
    public void setUltimoAngulo(int a) { ultimoAngulo = a; }

    public int getUltimoRaio() { return ultimoRaio; }
    public void setUltimoRaio(int r) { ultimoRaio = r; }
}
