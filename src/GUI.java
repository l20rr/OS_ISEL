import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.Beans;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JSpinner;
import javax.swing.JCheckBox;

public class GUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JTextField textField_Distancia;
    private JTextField textField_Angulo;
    private JTextField textField_Robot;
    private JTextField textField_Raio;
    private JTextArea textArea_console;

    private BaseDados db;   // agora dentro da classe GUI

    private void MyPrint(String msg) {
        textArea_console.append(msg + "\n"); // imprime na consola da GUI
    }

    /**
     * Create the frame.
     */
    public GUI(Application app) {
        db = app.getDB();
        // Configurações da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 702, 420);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

///////////////////////---------------------------------BTNs--------------------------------------
        // Botão "FRENTE"
        JButton btnFrente = new JButton("FRENTE");
        btnFrente.setBackground(Color.GREEN);
        btnFrente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (!Beans.isDesignTime() && db != null) {
                    db.getRobot().Reta(db.getDistancia());
                    db.getRobot().Parar(false);
                    MyPrint("fiz uma reta com " + db.getDistancia());
                }
            }
        });
        btnFrente.setBounds(237, 108, 99, 32);
        contentPane.add(btnFrente);

        // Movimento Trás
        JButton btnTras = new JButton("TRÁS");
        btnTras.setBackground(new Color(255, 128, 64));
        btnTras.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!Beans.isDesignTime() && db != null) {
                    db.getRobot().Reta(-(db.getDistancia()));
                    db.getRobot().Parar(false);
                    MyPrint("fiz uma marcha trás com " + db.getDistancia());
                }
            }
        });
        btnTras.setBounds(237, 171, 99, 32);
        contentPane.add(btnTras);

        // Movimento PARAR
        JButton btnParar = new JButton("PARAR");
        btnParar.setBackground(new Color(255, 0, 0));
        btnParar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                db.getRobot().Parar(true);
                MyPrint("robot parou");   
            }
        });
        btnParar.setBounds(237, 140, 99, 32);
        contentPane.add(btnParar);

        // Movimento direita
        JButton btnDir = new JButton("DIREITA");
        btnDir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                db.getRobot().CurvarDireita(db.getRaio(),db.getAngulo());
                db.getRobot().Parar(false);
                MyPrint("movimento direita acionado");
                db.getRobot().Parar(false);
                MyPrint("robot fez uma curva com angulo" + db.getAngulo() + " , e raio: " + db.getRaio() + " para direita");
            }
        });
        btnDir.setBackground(new Color(0, 0, 255));
        btnDir.setBounds(335, 140, 99, 32);
        contentPane.add(btnDir);

        // Movimento Esquerda
        JButton btnEsq = new JButton("ESQUERDA");
        btnEsq.setBackground(new Color(255, 128, 192));
        btnEsq.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            	db.getRobot().CurvarEsquerda(db.getRaio(),db.getAngulo());
            	MyPrint("movimento esquerda acionado");
            	db.getRobot().Parar(false);
            	   MyPrint("robot fez uma curva com angulo" + db.getAngulo() + " , e raio: " + db.getRaio() + " para esquerda");
                
            }
        });
        btnEsq.setBounds(139, 140, 99, 32);
        contentPane.add(btnEsq);
////-----------------------------------------------------------------------
     // CheckBox "Ligar"
        JCheckBox checkLigar = new JCheckBox("Ligar");
        checkLigar.setBounds(36, 36, 97, 23);
        contentPane.add(checkLigar);

        // Estado inicial de acordo com o banco
        checkLigar.setSelected(db.isRobotAberto());

        // Listener para ligar/desligar
        checkLigar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!Beans.isDesignTime() && db != null) {
                    if (db.isRobotAberto()) {
                        // Desligar
                        db.getRobot().CloseEV3();
                        db.setRobotAberto(false);
                        textField_Robot.setText("");
                    } else {
                        // Ligar
                        boolean aberto = db.getRobot().OpenEV3(db.getNomeRobot());
                        db.setRobotAberto(aberto);
                        textField_Robot.setText(db.getNomeRobot());
                    }
                    
                    // Mantém o estado do checkBox sincronizado
                    checkLigar.setSelected(db.isRobotAberto());
                    
                    MyPrint("o robo foi " + (db.isRobotAberto() ? "aberto" : "fechado"));
                }
            }
        });


///////////////////---------- textField -------------
// Distancia
        JLabel btnLabel_Distancia = new JLabel("Distância ");
        btnLabel_Distancia.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnLabel_Distancia.setBounds(369, 30, 70, 32);
        contentPane.add(btnLabel_Distancia);

        textField_Distancia = new JTextField();
        textField_Distancia.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!Beans.isDesignTime() && db != null) {
                    db.setDistancia(Integer.parseInt(textField_Distancia.getText()));
                    MyPrint("a distancia foi alterada para : " + db.getDistancia());
                }
            }
        });
        textField_Distancia.setBounds(430, 36, 50, 22);
        contentPane.add(textField_Distancia);
        if (!Beans.isDesignTime() && db != null) {
            textField_Distancia.setText("" + db.getDistancia());
        } else {
            textField_Distancia.setText("0");
        }
        textField_Distancia.setColumns(10);

// Ângulo
        JLabel btnLabel_Angulo = new JLabel("Ângulo");
        btnLabel_Angulo.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnLabel_Angulo.setBounds(166, 30, 70, 32);
        contentPane.add(btnLabel_Angulo);

        textField_Angulo = new JTextField();
        textField_Angulo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!Beans.isDesignTime() && db != null) {
                    db.setAngulo(Integer.parseInt(textField_Angulo.getText()));
                    MyPrint("o ângulo foi alterado para : " + db.getAngulo());
                }
            }
        });
        textField_Angulo.setColumns(10);
        textField_Angulo.setBounds(210, 36, 50, 22);
        contentPane.add(textField_Angulo);
        if (!Beans.isDesignTime() && db != null) {
            textField_Angulo.setText("" + db.getAngulo());
        } else {
            textField_Angulo.setText("0");
        }

// Robot name
        JLabel btnLabel_ROBOT = new JLabel("ROBOT");
        btnLabel_ROBOT.setBounds(516, 37, 56, 16);
        contentPane.add(btnLabel_ROBOT);

        textField_Robot = new JTextField();
        textField_Robot.setColumns(10);
        textField_Robot.setBounds(575, 31, 50, 22);
        contentPane.add(textField_Robot);

// Raio
        JLabel btnLabel_Raio = new JLabel("Raio");
        btnLabel_Raio.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnLabel_Raio.setBounds(277, 30, 34, 32);
        contentPane.add(btnLabel_Raio);

        textField_Raio = new JTextField();
        textField_Raio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!Beans.isDesignTime() && db != null) {
                    db.setRaio(Integer.parseInt(textField_Raio.getText()));
                    MyPrint("o raio foi alterado para : " + db.getRaio());
                }
            }
        });
        textField_Raio.setColumns(10);
        textField_Raio.setBounds(308, 36, 50, 22);
        contentPane.add(textField_Raio);
        if (!Beans.isDesignTime() && db != null) {
            textField_Raio.setText("" + db.getRaio());
        } else {
            textField_Raio.setText("0");
        }

//////////////////////*********Movimentos aleatroiros***********///////////////
        JLabel label_Numero = new JLabel("Número:");
        label_Numero.setFont(new Font("Tahoma", Font.PLAIN, 13));
        label_Numero.setBounds(415, 195, 65, 25);
        contentPane.add(label_Numero);

        JSpinner spinner = new JSpinner();
        spinner.setFont(new Font("Tahoma", Font.PLAIN, 13));
        spinner.setBounds(467, 191, 56, 32);
        contentPane.add(spinner);

        JRadioButton rdbtnMovAlt = new JRadioButton("Movimentos Aleatórios");
        rdbtnMovAlt.setBackground(Color.WHITE);
        rdbtnMovAlt.setBounds(529, 198, 135, 23);
        contentPane.add(rdbtnMovAlt);
        rdbtnMovAlt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (rdbtnMovAlt.isSelected()) {
                    // Lê o número de movimentos do spinner
                    int qtd = (int) spinner.getValue();
                    MyPrint("Gerando " + qtd + " movimentos aleatórios...");

                    try {
                        // Cria e inicia a thread de movimentos aleatórios
                    	MovimentosAleatorios tarefa = new MovimentosAleatorios(
                    	        db.getBuffer(), qtd);
                        tarefa.start();

                        MyPrint("movimento aleatorio ok") ;
                    } catch (Exception ex) {
                        MyPrint("Erro ao iniciar movimentos aleatórios: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            }
        });
        
////////////---------consola -------------
    JScrollPane scrollPane = new JScrollPane();
    scrollPane.setBounds(90, 246, 563, 95);
    contentPane.add(scrollPane);

    textArea_console = new JTextArea(); // inicialização aqui
    scrollPane.setViewportView(textArea_console);

    JLabel label_Consola = new JLabel("Consola:");
    label_Consola.setFont(new Font("Tahoma", Font.PLAIN, 13));
    label_Consola.setBounds(90, 221, 82, 16);
    contentPane.add(label_Consola);
        // Listener para fechar corretamente
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                if (!Beans.isDesignTime() && db != null) {
                    if (db.isRobotAberto())
                        db.getRobot().CloseEV3();
                    db.setTerminar(true);
                }
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