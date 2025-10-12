
public class BaseDados {
	private boolean terminar; 
	private RobotLegoEV3Simula robot;
	private boolean robotAberto;
	private int distancia; 
	private int angulo; 
	private int raio; 
	private String  nomeRobot;
	private BufferCircular Buffer; 
	
	public boolean isRobotAberto() {
		return robotAberto;
	}
	
	public void setRobotAberto(boolean robotAberto ) {
		this.robotAberto = robotAberto; 
	}
	
	public BaseDados() {
		robot = new RobotLegoEV3Simula();
		terminar = false; 
		nomeRobot = "EV7";
		robotAberto = false;
	}

	public RobotLegoEV3Simula getRobot() {
		return robot;
	}
/*
	public void setRobot(RobotLegoEV3 robot) {
		this.robot = robot;
	}
	*/
	public boolean isTerminar() {
		return terminar;
	}
	
	public void setTerminar(boolean terminar) {
		this.terminar = terminar ; 
	}

	public int getDistancia() {
		return distancia;
	}

	public void setDistancia(int distancia) {
		this.distancia = distancia;
	}

	public int getAngulo() {
		return angulo;
	}

	public void setAngulo(int angulo) {
		this.angulo = angulo;
	}

	public int getRaio() {
		return raio;
	}

	public void setRaio(int raio) {
		this.raio = raio;
	}

	public String getNomeRobot() {
		return nomeRobot;
	}

	public void setNomeRobot(String nomeRobot) {
		this.nomeRobot = nomeRobot;
	}
	

	
}
  
  