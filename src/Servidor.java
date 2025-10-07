import robot.RobotEV3;

public class Servidor extends Tarefa{
	private BufferCircular buffer;
	private RobotEV3 robot;
	private Comando c;
	
	public void Reta(int distancia) {
		buffer.inserirElemento(c(reta,2,3));
	}
	public void Parar(boolean b) {
		
	}
	public void CurvarDireita(int raio, int angulo) {
		
	}
	public void CurvarEsquerda(int raio, int angulo) {
		
	}
	public void CloseEV3() {

	}
	public boolean OpenEV3(String nomeRobot) {
		return false;
	}
	
}
