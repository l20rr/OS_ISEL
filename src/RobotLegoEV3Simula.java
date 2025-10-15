public class RobotLegoEV3Simula {

    private boolean isConnected;
    private String robotName;

    public RobotLegoEV3Simula() {
        this.isConnected = false;
        this.robotName = "SimulatedEV3";
    }

    /**
     * Simula abertura da ligação ao EV3
     * @param nome nome do robô
     * @return true se conectado com sucesso
     */
    public boolean OpenEV3(String nome) {
        this.robotName = nome != null ? nome : "SimulatedEV3";
        this.isConnected = true; // Corrige o problema
        System.out.println("[Simulação] Ligação simulada ao robot " + this.robotName);
        return true;
    }

    /**
     * Fecha ligação
     */
    public void CloseEV3() {
        System.out.println("[Simulação] Ligação encerrada ao robot " + this.robotName);
        this.isConnected = false; // Atualiza estado
    }

    public void Reta(int distancia) {
        if (isConnected) {
            System.out.println("[Simulação] Movendo em linha reta por " + distancia + " unidades.");
        } else {
            System.out.println("[Simulação] Robo não está conectado.");
        }
    }

    public void CurvarEsquerda(int raio, int angulo) {
        if (isConnected) {
            System.out.println("[Simulação] Curvando à esquerda com raio " + raio + " e ângulo " + angulo);
        } else {
            System.out.println("[Simulação] Robo não está conectado.");
        }
    }

    public void CurvarDireita(int raio, int angulo) {
        if (isConnected) {
            System.out.println("[Simulação] Curvando à direita com raio " + raio + " e ângulo " + angulo);
        } else {
            System.out.println("[Simulação] Robo não está conectado.");
        }
    }

    public void Parar(boolean immediateReturn) {
        if (isConnected) {
            System.out.println("[Simulação] Robo parou. (ImmediateReturn = " + immediateReturn + ")");
        } else {
            System.out.println("[Simulação] Robo não está conectado.");
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    public String getRobotName() {
        return robotName;
    }
}
