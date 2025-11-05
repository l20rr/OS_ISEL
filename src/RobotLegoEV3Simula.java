public class RobotLegoEV3Simula {

    private boolean isConnected;
    private String robotName;


    public final int S_1 = 1; // identificador do sensor
    private boolean sensorAtivoSimula = true; // ativo o tempo todo
    private boolean ultimoToque = false;
    public RobotLegoEV3Simula() {
        this.isConnected = false;
        this.robotName = "SimulatedEV3";
    }


    public int SensorToque(int sensor) {
        if (!isConnected) return 0;
        return (ultimoToque ? 1 : 0);
    }

    // simula um toque (é aqui que o "clicar" será sentido)
    public void simularToque() {
        if (!isConnected) {
            System.out.println("[Simulação] Robô não está ligado — toque ignorado.");
            return;
        }

        ultimoToque = true;
      

        // apaga o toque logo depois (para que o próximo clique volte a funcionar)
        new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            ultimoToque = false;
        }).start();
    }
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
