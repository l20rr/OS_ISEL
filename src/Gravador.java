/*
 * Criar buffer2 
 * enviar os comandos que o usuario fez para o novo buffer
 * buffer <-> gravador (comunicação direta)
 * GUI2(parar) vai enviar os comandos do buffer para um arquivo .txt
 * Robot le esse arquivo 
 * 
 * !!!! ter ToString para os comandos e para o buffer2!!!! 
 * */
import java.io.FileWriter;
import java.io.IOException;
    
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

    
    public void registarComando(Comando comando) {
        if (gravando) {
            bufferRec.inserirElemento(comando);
        }
    }
    
    public void fileWriter() {
    	try (FileWriter writer = new FileWriter("Comandos.txt")) {
    		 String conteudo_comandos = bufferToString(); 
            writer.write(conteudo_comandos);
            System.out.println("Arquivo escrito com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao escrever no arquivo: " + e.getMessage());
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

