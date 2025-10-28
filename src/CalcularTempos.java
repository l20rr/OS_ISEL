public class CalcularTempos {
	
	private final static double vel = 0.02;
	private final static int comunicacao = 100;
	
	
	public static int tempoReta(int d) {
        // d está em cm, vel em cm/ms → resultado em ms
        return (int) ((d / vel) + comunicacao);
    }
	
	public static int tempoCurva(int r, int a) {
		double rad = 2 * Math.PI * r * (a / 360.0);
		return (int) ((rad/vel) + comunicacao);
	}
	
	public static int tempoParar() {
		return comunicacao;
	}

}