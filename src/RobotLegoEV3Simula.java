
public class RobotLegoEV3Simula {

    private boolean isConnected;
    private String robotName;

    public RobotLegoEV3Simula() {
        this.isConnected = false;
        this.robotName = "SimulatedEV3";
    }

    // Simula a abertura da ligação ao EV3
    public boolean OpenEV3(String name) {
        this.robotName = name;
        this.isConnected = true;
        System.out.println("Simulação: Conectado ao robô " + name);
        return true;
    }

    // Simula o fecho da ligação
    public void CloseEV3() {
        if (isConnected) {
            System.out.println("Simulação: Ligação com " + robotName + " terminada.");
            isConnected = false;
        } else {
            System.out.println("Simulação: Nenhum robô conectado.");
        }
    }

    // Simula movimento em linha reta
    public void Reta(int distancia) {
        if (isConnected) {
            System.out.println("Simulação: Movendo em linha reta por " + distancia + " unidades.");
        } else {
            System.out.println("Simulação: Robo não está conectado.");
        }
    }

    // Simula movimento circular à esquerda
    public void CurvarEsquerda(int raio, int angulo) {
        if (isConnected) {
            System.out.println("Simulação: Curvando à esquerda com raio " + raio + " e ângulo " + angulo);
        } else {
            System.out.println("Simulação: Robo não está conectado.");
        }
    }

    // Simula movimento circular à direita
    public void CurvarDireita(int raio, int angulo) {
        if (isConnected) {
            System.out.println("Simulação: Curvando à direita com raio " + raio + " e ângulo " + angulo);
        } else {
            System.out.println("Simulação: Robo não está conectado.");
        }
    }

    // Simula parar o robô
    public void Parar(boolean immediateReturn) {
        if (isConnected) {
            System.out.println("Simulação: Robo parou. (ImmediateReturn = " + immediateReturn + ")");
        } else {
            System.out.println("Simulação: Robo não está conectado.");
        }
    }

    // Getter para estado de conexão
    public boolean isConnected() {
        return isConnected;
    }

    public String getRobotName() {
        return robotName;
    }


}
