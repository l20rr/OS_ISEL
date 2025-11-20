/*
 * Criar buffer2 
 * enviar os comandos que o usuario fez para o novo buffer
 * buffer <-> gravador (comunicação direta)
 * GUI2(parar) vai enviar os comandos do buffer para um arquivo .txt
 * Robot le esse arquivo 
 * 
 * !!!! ter ToString para os comandos e para o buffer2!!!! 
 * */

    
public class Gravador {

    private final BufferRec bufferRec;
    private volatile boolean gravando = false;

    public Gravador(BufferRec bufferRec) {
        this.bufferRec = bufferRec;
    }

    // Ativa ou desativa a gravação
    public void setGravando(boolean gravando) {
        this.gravando = gravando;
    }

    public boolean isGravando() {
        return gravando;
    }

    
    public void registrarComando(Comando comando) {
        if (gravando) {
            bufferRec.inserirElemento(comando);
        }
    }

    
    public String bufferToString() {
        StringBuilder sb = new StringBuilder();
        while (!bufferRec.estaVazio()) {
            Comando c = bufferRec.removerElemento();
            if (c != null) {
                sb.append(c.toString()).append("\n");
            }
        }
        return sb.toString();
    }
}

