/*
 * Criar buffer2 
 * enviar os comandos que o usuario fez para o novo buffer
 * buffer <-> gravador (comunicação direta)
 * GUI2(parar) vai enviar os comandos do buffer para um arquivo .txt
 * Robot le esse arquivo 
 * 
 * !!!! ter ToString para os comandos e para o buffer2!!!! 
 * */
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
    
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
        try {
            OutputStream os = new FileOutputStream("Comandos.txt");
            Writer writer = new OutputStreamWriter(os, "UTF-8");

            String conteudo_comandos = bufferToString();
            writer.write(conteudo_comandos);

            writer.close();
            os.close();

            System.out.println("Arquivo escrito com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }
    public String fileReader() {
        StringBuilder sb = new StringBuilder();
        try (Reader reader = new InputStreamReader(new FileInputStream("Comandos.txt"), "UTF-8")) {
            int c;
            while ((c = reader.read()) != -1) {
                sb.append((char) c);
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
        return sb.toString();
    }


    
    public String bufferToString() {
        StringBuilder sb = new StringBuilder();
        while (!bufferRec.equals(0)) {
            Comando c = bufferRec.removerElemento();
            if (c != null) {
                sb.append(c.toString()).append("\n");
            }
        }
        return sb.toString();
    }
}

