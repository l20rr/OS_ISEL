import java.util.Random;

/**
 * MovimentosAleatorios - gera comandos aleatórios e envia para o Servidor.
 */
public class MovimentosAleatorios extends Tarefa {

    private final BaseDados db;
    private final int quantidadeComandos;
    private final Random rand;

    // Constantes físicas para simulação (ajuste conforme avaliação)
    private static final double VELOCIDADE_CM_S = 20.0;  // cm/s
    private static final int TEMPO_COMUNICACAO_MS = 100;  // ms entre comandos

    public MovimentosAleatorios(BaseDados db, int quantidadeComandos) {
        this.db = db;
        this.quantidadeComandos = quantidadeComandos;
        this.rand = new Random();
        this.ativa = true;
    }

    /** Pausa a geração de movimentos */
    public void pararMovimentos() {
        ativa = false;
        System.out.println("[MovimentosAleatorios] Movimentos aleatórios pausados.");
    }

    @Override
    public void run() {
        System.out.println("[MovimentosAleatorios] Thread iniciada com " + quantidadeComandos + " comandos por ciclo.");

        while (isAtiva()) {
            // Verifica se servidor existe e está ativo
            if (db.getServidor() == null || !db.getServidor().isAtiva()) {
                System.out.println("[MovimentosAleatorios] Servidor inativo, aguardando...");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    break;
                }
                continue;
            }

            int totalTempoMs = 0;

            for (int i = 0; i < quantidadeComandos && isAtiva(); i++) {
                int tempoComando = gerarComandoAleatorio();
                totalTempoMs += tempoComando;

                // Intervalo curto entre comandos para evitar sobrecarga no buffer
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    return;
                }
            }

            
            try {
                Thread.sleep(Math.max(200, totalTempoMs));
            } catch (InterruptedException e) {
                break;
            }

            
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                break;
            }
        }

        System.out.println("[MovimentosAleatorios] Thread finalizada.");
    }

  
    private int gerarComandoAleatorio() {
        int tipo = rand.nextInt(3); // 0 = RETA, 1 = CURVA DIREITA, 2 = CURVA ESQUERDA
        int tempoEstimado = 0;

        if (db.getServidor() == null) return 0;

        switch (tipo) {
            case 0: { // RETA
                int distancia = 10 + rand.nextInt(41); // 10 a 50 cm
                tempoEstimado = calculaTempoReta(distancia);
                System.out.printf("[MovimentosAleatorios] Reta: %d cm (%.1f s)%n",
                        distancia, tempoEstimado / 1000.0);
                db.getServidor().Reta(distancia);
                break;
            }
            case 1: { // CURVA DIREITA
                int raio = 10 + rand.nextInt(21);   // 10 a 30 cm
                int angulo = 20 + rand.nextInt(71); // 20 a 90 graus
                tempoEstimado = calculaTempoCurva(raio, angulo);
                System.out.printf("[MovimentosAleatorios] Curva Direita: raio=%d cm, angulo=%d° (%.1f s)%n",
                        raio, angulo, tempoEstimado / 1000.0);
                db.getServidor().CurvarDireita(raio, angulo);
                break;
            }
            case 2: { // CURVA ESQUERDA
                int raio = 10 + rand.nextInt(21);
                int angulo = 20 + rand.nextInt(71);
                tempoEstimado = calculaTempoCurva(raio, angulo);
                System.out.printf("[MovimentosAleatorios] Curva Esquerda: raio=%d cm, angulo=%d° (%.1f s)%n",
                        raio, angulo, tempoEstimado / 1000.0);
                db.getServidor().CurvarEsquerda(raio, angulo);
                break;
            }
        }

        return tempoEstimado;
    }

    /* -------------------- Cálculos de tempo -------------------- */

    private int calculaTempoReta(int distanciaCm) {
        double tempoSeg = distanciaCm / VELOCIDADE_CM_S;
        return (int) (tempoSeg * 1000) + TEMPO_COMUNICACAO_MS;
    }

    private int calculaTempoCurva(int raioCm, int anguloGraus) {
        double comprimentoArco = (anguloGraus / 360.0) * 2.0 * Math.PI * raioCm;
        double tempoSeg = comprimentoArco / VELOCIDADE_CM_S;
        return (int) (tempoSeg * 1000) + TEMPO_COMUNICACAO_MS;
    }
}
