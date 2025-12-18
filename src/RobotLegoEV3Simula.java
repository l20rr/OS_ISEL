public class RobotLegoEV3Simula {

    private boolean isConnected;
    private String robotName;

    public final int S_1 = 1; // identificador do sensor

    // 🔴 estado do sensor de toque (simulado)
    private volatile boolean ultimoToque = false;

    public RobotLegoEV3Simula() {
        this.isConnected = false;
        this.robotName = "SimulatedEV3";
    }

    // ================= SENSOR =================
    public int SensorToque(int sensor) {
        if (!isConnected) return 0;

        // comportamento igual ao sensor real
        if (ultimoToque) {
            ultimoToque = false; // consome o toque
            return 1;
        }
        return 0;
    }

    // 🔘 chamado pela GUI
    public void simularToque() {
        if (!isConnected) {
            System.out.println("[Simulação] Robô não ligado — toque ignorado.");
            return;
        }

        System.out.println("[Simulação] Toque simulado.");
        ultimoToque = true;
    }

    // ================= CONEXÃO =================
    public boolean OpenEV3(String nome) {
        this.robotName = (nome != null ? nome : "SimulatedEV3");
        this.isConnected = true;
        System.out.println("[Simulação] Ligado ao robô " + robotName);
        return true;
    }

    public void CloseEV3() {
        System.out.println("[Simulação] Ligação encerrada ao robô " + robotName);
        this.isConnected = false;
    }

    // ================= MOVIMENTOS =================
    public void Reta(int distancia) {
        if (isConnected)
            System.out.println("[Simulação] Reta: " + distancia);
    }

    public void CurvarEsquerda(int raio, int angulo) {
        if (isConnected)
            System.out.println("[Simulação] Curva esquerda r=" + raio + " a=" + angulo);
    }

    public void CurvarDireita(int raio, int angulo) {
        if (isConnected)
            System.out.println("[Simulação] Curva direita r=" + raio + " a=" + angulo);
    }

    public void Parar(boolean immediateReturn) {
        if (isConnected)
            System.out.println("[Simulação] Parar (immediate=" + immediateReturn + ")");
    }

    public boolean isConnected() {
        return isConnected;
    }
}
