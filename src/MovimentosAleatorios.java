//Produtor
import java.util.Random;

public class MovimentosAleatorios extends Tarefa {
    private BufferCircular buffer;
    private int quantidadeComandos; // Número de comandos a gerar
    private Random rand;


    // Constantes do robot
    private final double VELOCIDADE = 20.0; // cm/s
    private final int TEMPO_COMUNICACAO = 100; // ms

    public MovimentosAleatorios(BufferCircular buffer, int quantidadeComandos) {
        this.buffer = buffer;
        this.quantidadeComandos = quantidadeComandos;
        this.rand = new Random();
    }

    @Override
    public void run() {
        while (isAtiva()) {
            int totalTempo = 0;

            for (int i = 0; i < quantidadeComandos; i++) {
                Comando c = gerarComandoAleatorio();
                buffer.inserirElemento(c);

                int tempoComando = calcularTempoComando(c);
                totalTempo += tempoComando;

                 }

            // Após enviar todos os comandos, esperar o tempo de execução
            try {
                Thread.sleep(totalTempo);
            } catch (InterruptedException e) {
                break;
            }

            // Inserir comando de parar
            Comando parar = new Comando("PARAR", 0, 0);
            buffer.inserirElemento(parar);
           
            
            // Espera antes de gerar uma nova sequência (opcional)
            try {
                Thread.sleep(500); 
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private Comando gerarComandoAleatorio() {
        int tipo = rand.nextInt(3); // 0 = RETA, 1 = CURVA DIREITA, 2 = CURVA ESQUERDA

        switch (tipo) {
            case 0: // RETA
                int distancia = 10 + rand.nextInt(41); // 10 a 50 cm
                return new Comando("RETA", distancia, 0);
            case 1: // CURVA DIREITA
                int raioD = 10 + rand.nextInt(21); // 10 a 30 cm
                int anguloD = 20 + rand.nextInt(71); // 20 a 90 graus
                return new Comando("CURVA_DIREITA", raioD, anguloD);
            case 2: // CURVA ESQUERDA
                int raioE = 10 + rand.nextInt(21);
                int anguloE = 20 + rand.nextInt(71);
                return new Comando("CURVA_ESQUERDA", raioE, anguloE);
            default:
                return null; // nunca acontece
        }
    }

    private int calcularTempoComando(Comando c) {
        double tempo = 0;
        switch (c.tipo) {
            case "RETA":
                tempo = (c.arg1 / VELOCIDADE) * 1000 + TEMPO_COMUNICACAO; // cm / (cm/s) -> s -> ms
                break;
            case "CURVA_DIREITA":
            case "CURVA_ESQUERDA":
                double anguloRad = Math.toRadians(c.arg2);
                tempo = (c.arg1 * anguloRad) / VELOCIDADE * 1000 + TEMPO_COMUNICACAO;
                break;
            case "PARAR":
                tempo = TEMPO_COMUNICACAO;
                break;
        }
        return (int) tempo;
    }
}
