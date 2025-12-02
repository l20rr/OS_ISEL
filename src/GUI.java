import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.Beans;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private boolean sensorAtivoSimulado = false;

    private JTextField textField_Distancia;
    private JTextField textField_Angulo;
    private JTextField textField_Robot;
    private JTextField textField_Raio;
    private JTextArea textArea_console; 
    private Gravador gravador;

    private BaseDados db;   
    private MovimentosAleatorios movimentoAleatorioAtivo = null;

    public void MyPrint(String msg) {
        SwingUtilities.invokeLater(() -> textArea_console.append(msg + "\n"));
    }
    private EvitarObstaculo evitar; 
    private JTextField textField;


    public GUI(Application app) {
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
            if (!db.isRobotAberto()) {
                boolean aberto = db.getServidor().OpenEV3(db.getNomeRobot());
                db.setRobotAberto(aberto);
                if (aberto) {
                    db.getServidor().desbloquear();
                    textField_Robot.setText(db.getNomeRobot());
                    MyPrint("Servidor desbloqueado e ativo.");
                    if (evitar == null) evitar = new EvitarObstaculo(db);
                }
            }
            // Se já estiver aberto, NÃO DESLIGUE O ROBÔ
            checkLigar.setSelected(db.isRobotAberto());
        });



        contentPane.add(checkLigar);

        // ------------------- CAMPOS ----------------------
        // Distância
        JLabel lblDistancia = new JLabel("Distância");
        lblDistancia.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblDistancia.setBounds(369, 30, 70, 32);
        contentPane.add(lblDistancia);

        textField_Distancia = new JTextField("" + db.getUltimaDistancia());
        textField_Distancia.setBounds(430, 36, 50, 22);
        textField_Distancia.setColumns(10);
        textField_Distancia.addActionListener(e -> {
            db.setUltimaDistancia(Integer.parseInt(textField_Distancia.getText()));
            MyPrint("a distancia foi alterada para : " + db.getUltimaDistancia());
        });
        contentPane.add(textField_Distancia);

        // Ângulo
        JLabel lblAngulo = new JLabel("Ângulo");
        lblAngulo.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblAngulo.setBounds(166, 30, 70, 32);
        contentPane.add(lblAngulo);

        textField_Angulo = new JTextField("" + db.getUltimoAngulo());
        textField_Angulo.setColumns(10);
        textField_Angulo.setBounds(210, 36, 50, 22);
        textField_Angulo.addActionListener(e -> {
            db.setUltimoAngulo(Integer.parseInt(textField_Angulo.getText()));
            MyPrint("o ângulo foi alterado para : " + db.getUltimoAngulo());
        });
        contentPane.add(textField_Angulo);

        // Raio
        JLabel lblRaio = new JLabel("Raio");
        lblRaio.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblRaio.setBounds(277, 30, 34, 32);
        contentPane.add(lblRaio);

        textField_Raio = new JTextField("" + db.getUltimoRaio());
        textField_Raio.setColumns(10);
        textField_Raio.setBounds(308, 36, 50, 22);
        textField_Raio.addActionListener(e -> {
            db.setUltimoRaio(Integer.parseInt(textField_Raio.getText()));
            MyPrint("o raio foi alterado para : " + db.getUltimoRaio());
        });
        contentPane.add(textField_Raio);

        // Robot
        JLabel lblRobot = new JLabel("ROBOT");
        lblRobot.setBounds(516, 37, 56, 16);
        contentPane.add(lblRobot);

        textField_Robot = new JTextField();
        textField_Robot.setColumns(10);
        textField_Robot.setBounds(575, 31, 50, 22);
        contentPane.add(textField_Robot);

        // ------------------- MOVIMENTOS ALEATÓRIOS ----------------------
        JLabel lblNumero = new JLabel("Número:");
        lblNumero.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblNumero.setBounds(475, 195, 65, 25);
        contentPane.add(lblNumero);

        JSpinner spinner = new JSpinner();
        spinner.setFont(new Font("Tahoma", Font.PLAIN, 13));
        spinner.setBounds(527, 191, 56, 32);
        contentPane.add(spinner);

        JRadioButton rdbtnMovAlt = new JRadioButton("Movimentos Aleatórios");
        rdbtnMovAlt.setBackground(Color.WHITE);
        rdbtnMovAlt.setBounds(460, 160, 210, 25);
        contentPane.add(rdbtnMovAlt);

        rdbtnMovAlt.addActionListener(e -> {
        	if (!rdbtnMovAlt.isSelected()) {
        	    if (movimentoAleatorioAtivo != null) {
        	    	movimentoAleatorioAtivo.interrupt();
        	    	movimentoAleatorioAtivo.bloquear(); 
        	        movimentoAleatorioAtivo = null;
        	        MyPrint("Movimentos aleatórios bloqueados.");
        	    }

        	    db.getServidor().Parar(true); // para completamente e limpa buffer
        	    MyPrint("Parada forçada após desativar movimentos aleatórios.");
        	    return;
        	}


            int qtd = (int) spinner.getValue();
            movimentoAleatorioAtivo = new MovimentosAleatorios(db, qtd, this);
            movimentoAleatorioAtivo.start();       // inicia uma única vez
            movimentoAleatorioAtivo.desbloquear(); // começa o loop de runing()
            MyPrint("Gerando " + qtd + " movimentos aleatórios...");
        });
        // ------------------- CONSOLE ----------------------
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(75, 290, 520, 160);
        contentPane.add(scrollPane);
        
                textArea_console = new JTextArea();
                scrollPane.setRowHeaderView(textArea_console);

        JLabel lblConsole = new JLabel("Consola:");
        lblConsole.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblConsole.setBounds(65, 270, 82, 16);
        contentPane.add(lblConsole);
                
        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.setBounds(170, 460, 100, 28);
        contentPane.add(btnLimpar);
                
        JCheckBox chckbxImprimirCheckBox = new JCheckBox("Imprimir");
        chckbxImprimirCheckBox.setBounds(410, 460, 100, 28);
        contentPane.add(chckbxImprimirCheckBox);

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