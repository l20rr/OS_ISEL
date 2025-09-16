
public class BaseDados {
	private boolean terminar; 
	private RobotLegoEV3 robot;
	private boolean robotAberto;
	private int distancia; 
	
	public boolean isRobotAberto() {
		return robotAberto;
	}
	
	public void setRobotAberto(boolean robotAberto ) {
		this.robotAberto = robotAberto; 
	}
	
	public BaseDados() {
		robot = new RobotLegoEV3();
		terminar = false; 
		robotAberto = false;
	}

	public RobotLegoEV3 getRobot() {
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
	
}
  
  