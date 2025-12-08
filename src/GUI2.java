import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.Beans;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI2 extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private boolean sensorAtivoSimulado = false;

    private JTextField textField_Distancia2;
    private JTextField textField_Angulo2;
    private JTextField textField_Robot2;
    private JTextField textField_Raio2;
    private JTextArea textArea_console2; 
    private Gravador gravador;

    private BaseDados db;   
    private MovimentosAleatorios movimentoAleatorioAtivo = null;

    public void MyPrint(String msg) {
        SwingUtilities.invokeLater(() -> textArea_console2.append(msg + "\n"));
    }
    private EvitarObstaculo evitar; //mover para o topo da classe
    private JTextField textField;
    private final JFileChooser fileChooser = new JFileChooser();


    public GUI2(Application app) {
    	 this.db = app.getDB();
    	 this.gravador = db.getGravador();
    	 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	    setBounds(100, 100, 673, 644);

        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // ------------------- BOTÕES ----------------------
        JButton btnFrente = new JButton("FRENTE");
        btnFrente.setBackground(Color.GREEN);
        btnFrente.setBounds(237, 108, 99, 32);
        btnFrente.addActionListener(e -> {
        	try {
        	    db.getServidor().s.acquire();
        	    db.getServidor().Reta(db.getUltimaDistancia());
        	    db.getServidor().Parar(false);
        	} catch (InterruptedException e1) {
        	    Thread.currentThread().interrupt();
        	} finally {
        	    db.getServidor().s.release();
        	}
            MyPrint("fiz uma reta com " + db.getUltimaDistancia());
            Comando comando = new Comando("FRENTE", db.getUltimaDistancia(), 0);
            gravador.registarComando(comando);
        });
        contentPane.add(btnFrente);

        JButton btnTras = new JButton("TRÁS");
        btnTras.setBackground(new Color(255, 128, 64));
        btnTras.setBounds(237, 171, 99, 32);
        btnTras.addActionListener(e -> {
        	try {
        	    db.getServidor().s.acquire();
        	    db.getServidor().Reta(- db.getUltimaDistancia());
        	    db.getServidor().Parar(false);
        	} catch (InterruptedException e1) {
        	    Thread.currentThread().interrupt();
        	} finally {
        	    db.getServidor().s.release();
        	}
            MyPrint("fiz uma marcha trás com " + db.getUltimaDistancia());
            Comando comando = new Comando("TRAS", db.getUltimaDistancia(), 0);
            gravador.registarComando(comando);
        });
        contentPane.add(btnTras);

        JButton btnParar = new JButton("PARAR");
        btnParar.setBackground(Color.RED);
        btnParar.setBounds(237, 140, 99, 32);
        btnParar.addActionListener(e -> {
            if (movimentoAleatorioAtivo != null) {
                movimentoAleatorioAtivo.bloquear();
                movimentoAleatorioAtivo = null;
                MyPrint("Movimentos aleatórios bloqueados.");
            }

            db.getServidor().Parar(true); // agora é seguro
            MyPrint("Robô parado (forçado), buffer limpo, servidor continua ativo.");
            Comando comando = new Comando("PARAR", 0, 0);
            gravador.registarComando(comando);
        });

        contentPane.add(btnParar);
        JButton btnDir = new JButton("DIREITA");
        btnDir.setBackground(Color.BLUE);
        btnDir.setBounds(335, 140, 99, 32);
        btnDir.addActionListener(e -> {
        	try {
        	    db.getServidor().s.acquire();
        	    db.getServidor().CurvarDireita(db.getUltimoRaio(), db.getUltimoAngulo());
        	    db.getServidor().Parar(false);
        	} catch (InterruptedException e1) {
        	    Thread.currentThread().interrupt();
        	} finally {
        	    db.getServidor().s.release();
        	}
            MyPrint("robot fez uma curva direita com ângulo " + db.getUltimoAngulo() + " e raio " + db.getUltimoRaio());
            Comando comando = new Comando("DIREITA", db.getUltimoRaio(), db.getUltimoAngulo());
            gravador.registarComando(comando);
        });
        contentPane.add(btnDir);

        JButton btnEsq = new JButton("ESQUERDA");
        btnEsq.setBackground(new Color(255, 128, 192));
        btnEsq.setBounds(139, 140, 99, 32);
        btnEsq.addActionListener(e -> {
        	try {
        	    db.getServidor().s.acquire();
        	    db.getServidor().CurvarEsquerda(db.getUltimoRaio(), db.getUltimoAngulo());
        	    db.getServidor().Parar(false);
        	} catch (InterruptedException e1) {
        	    Thread.currentThread().interrupt();
        	} finally {
        	    db.getServidor().s.release();
        	}
            MyPrint("robot fez uma curva esquerda com ângulo " + db.getUltimoAngulo() + " e raio " + db.getUltimoRaio());
            Comando comando = new Comando("ESQUERDA", db.getUltimoRaio(), db.getUltimoAngulo());
            gravador.registarComando(comando);
        });
        contentPane.add(btnEsq);

        // ------------------- CHECKBOX LIGAR ----------------------
        JCheckBox checkLigar = new JCheckBox("Ligar");
        checkLigar.setBounds(36, 36, 97, 23);
        checkLigar.setBackground(Color.WHITE);
        checkLigar.setSelected(db.isRobotAberto());
        checkLigar.addActionListener(e -> {
            if (db.isRobotAberto()) {
                // Desligar o robô
                db.getServidor().CloseEV3();
                db.setRobotAberto(false);
                textField_Robot2.setText("");
                MyPrint("Servidor bloqueado e conexão encerrada.");

                if (evitar != null) evitar.bloquear();

            } else {
                // Ligar o robô
                boolean aberto = db.getServidor().OpenEV3(db.getNomeRobot());
                db.setRobotAberto(aberto);
                if (aberto) {
                    db.getServidor().desbloquear();
                    textField_Robot2.setText(db.getNomeRobot());
                    MyPrint("Servidor desbloqueado e ativo.");

                    // Evitar já foi criado na Application, apenas desbloquear
                    if (evitar != null) evitar.desbloquear(); 
                } else {
                    MyPrint("Falha ao abrir conexão com o robô.");
                }
            }
            checkLigar.setSelected(db.isRobotAberto());
        });



        contentPane.add(checkLigar);

        // ------------------- CAMPOS ----------------------
        // Distância
        JLabel lblDistancia = new JLabel("Distância");
        lblDistancia.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblDistancia.setBounds(369, 30, 70, 32);
        contentPane.add(lblDistancia);

        textField_Distancia2 = new JTextField("" + db.getUltimaDistancia());
        textField_Distancia2.setBounds(430, 36, 50, 22);
        textField_Distancia2.setColumns(10);
        textField_Distancia2.addActionListener(e -> {
            db.setUltimaDistancia(Integer.parseInt(textField_Distancia2.getText()));
            MyPrint("a distancia foi alterada para : " + db.getUltimaDistancia());
        });
        contentPane.add(textField_Distancia2);

        // Ângulo
        JLabel lblAngulo = new JLabel("Ângulo");
        lblAngulo.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblAngulo.setBounds(166, 30, 70, 32);
        contentPane.add(lblAngulo);

        textField_Angulo2 = new JTextField("" + db.getUltimoAngulo());
        textField_Angulo2.setColumns(10);
        textField_Angulo2.setBounds(210, 36, 50, 22);
        textField_Angulo2.addActionListener(e -> {
            db.setUltimoAngulo(Integer.parseInt(textField_Angulo2.getText()));
            MyPrint("o ângulo foi alterado para : " + db.getUltimoAngulo());
        });
        contentPane.add(textField_Angulo2);

        // Raio
        JLabel lblRaio = new JLabel("Raio");
        lblRaio.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblRaio.setBounds(277, 30, 34, 32);
        contentPane.add(lblRaio);

        textField_Raio2 = new JTextField("" + db.getUltimoRaio());
        textField_Raio2.setColumns(10);
        textField_Raio2.setBounds(308, 36, 50, 22);
        textField_Raio2.addActionListener(e -> {
            db.setUltimoRaio(Integer.parseInt(textField_Raio2.getText()));
            MyPrint("o raio foi alterado para : " + db.getUltimoRaio());
        });
        contentPane.add(textField_Raio2);

        // Robot
        JLabel lblRobot = new JLabel("ROBOT");
        lblRobot.setBounds(516, 37, 56, 16);
        contentPane.add(lblRobot);

        textField_Robot2 = new JTextField();
        textField_Robot2.setColumns(10);
        textField_Robot2.setBounds(575, 31, 50, 22);
        contentPane.add(textField_Robot2);

        
        // ------------------- CONSOLE ----------------------
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(94, 407, 456, 128);
        contentPane.add(scrollPane);
        
                textArea_console2 = new JTextArea();
                scrollPane.setRowHeaderView(textArea_console2);

        JLabel lblConsole = new JLabel("Consola:");
        lblConsole.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblConsole.setBounds(65, 379, 82, 16);
        contentPane.add(lblConsole);
                
        JLabel Ficheiro = new JLabel("Ficheiro");
        Ficheiro.setBounds(101, 273, 70, 23);
        contentPane.add(Ficheiro);

        textField = new JTextField();
        textField.setBounds(166, 275, 316, 21);
        contentPane.add(textField);
        textField.setColumns(10);
        
        JToggleButton file_btn = new JToggleButton("...");
        file_btn.setBounds(499, 271, 70, 27);
        contentPane.add(file_btn);

        file_btn.addItemListener(e -> {
        	   if (file_btn.isSelected()) {

        	        int result = fileChooser.showOpenDialog(contentPane);

        	        if (result == JFileChooser.APPROVE_OPTION) {
        	            File arquivo = fileChooser.getSelectedFile();
        	            textField.setText(arquivo.getAbsolutePath()); // mostra caminho completo
        	             }

        	        file_btn.setSelected(false); 
        	    }
        });

        
        
        JToggleButton tglbtnGravar = new JToggleButton("Gravar");
        tglbtnGravar.setBounds(228, 320, 147, 27);
        contentPane.add(tglbtnGravar);

        tglbtnGravar.addItemListener(e -> {
            if (tglbtnGravar.isSelected()) {
                tglbtnGravar.setText("Parar");
                gravador.setGravando(true); // ativa gravação
                MyPrint("Gravação iniciada.");
            } else {
                tglbtnGravar.setText("Gravar");
                gravador.setGravando(false); // desativa gravação
                MyPrint("Gravação parada.");
                
                gravador.fileWriter(); 
            }
        });        
                JCheckBox chckbxImprimirCheckBox = new JCheckBox("Imprimir");
                chckbxImprimirCheckBox.addActionListener(e -> {
                    String comandos = gravador.fileReader(); // lê o arquivo
                    MyPrint("Comandos no arquivo:\n" + comandos);
                });

                chckbxImprimirCheckBox.setBounds(408, 576, 93, 21);
                contentPane.add(chckbxImprimirCheckBox);
              
                
                JButton btnLimpar = new JButton("Limpar");
                btnLimpar.setBounds(166, 576, 85, 21);
                contentPane.add(btnLimpar);
             
             

        // ------------------- WINDOW CLOSE ----------------------
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (db.isRobotAberto()) {
                    db.getServidor().CloseEV3();
                }
               
                db.setTerminar(true);
            }
        });
    }

    public BaseDados getDB() {
        return db;
    }

    public void setDB(BaseDados db) {
        this.db = db;
    }
}

