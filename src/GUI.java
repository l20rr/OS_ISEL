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

public class GUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private BaseDados db;
    private JTextField textField_Distancia;
    private JTextField textField_Angulo;
    private JTextField textField_Robot;
    private JTextField textField_Raio;

    private void MyPrint(String msg) {
        
        System.out.println(msg);
    }

    /**
     * Create the frame.
     */
    public GUI() {

        // só inicializa BaseDados se não estiver no Design Mode
        if (!Beans.isDesignTime()) {
            db = new BaseDados();
        }

        // Configurações da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 702, 420);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Botão "Reta"
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

        // Botao On Off
        JRadioButton rdbtnOnOff = new JRadioButton("On/Off");
        rdbtnOnOff.setBackground(new Color(0, 128, 0));
        rdbtnOnOff.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!Beans.isDesignTime() && db != null) {
                    if (db.isRobotAberto()) {
                        db.getRobot().CloseEV3();
                        db.setRobotAberto(false);
                    } else {
                        db.setRobotAberto(db.getRobot().OpenEV3("EV2"));
                    }
                    rdbtnOnOff.setSelected(db.isRobotAberto());
                    MyPrint("o robo foi " + (db.isRobotAberto() ? "aberto" : "fechado"));
                }
            }
        });
        rdbtnOnOff.setBounds(36, 30, 109, 23);
        contentPane.add(rdbtnOnOff);

        JLabel btnLabel_ROBOT = new JLabel("ROBOT");
        btnLabel_ROBOT.setBounds(36, 61, 56, 16);
        contentPane.add(btnLabel_ROBOT);

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
        
        JButton btnTras = new JButton("TRÁS");
        btnTras.setBackground(Color.RED);
        btnTras.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        btnTras.setBounds(237, 171, 99, 32);
        contentPane.add(btnTras);
        
        JButton btnParar = new JButton("PARAR");
        btnParar.setBackground(Color.ORANGE);
        btnParar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        btnParar.setBounds(237, 140, 99, 32);
        contentPane.add(btnParar);
        
        JButton btnDir = new JButton("DIREITA");
        btnDir.setBackground(Color.CYAN);
        btnDir.setBounds(335, 140, 99, 32);
        contentPane.add(btnDir);
        
        JButton btnEsq = new JButton("ESQUERDA");
        btnEsq.setBackground(Color.CYAN);
        btnEsq.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        btnEsq.setBounds(139, 140, 99, 32);
        contentPane.add(btnEsq);
        
        JRadioButton rdbtnNewRadioButton = new JRadioButton("Movimentos Aleatórios");
        rdbtnNewRadioButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
        rdbtnNewRadioButton.setBounds(533, 187, 149, 40);
        contentPane.add(rdbtnNewRadioButton);
        
        textField_Angulo = new JTextField();
        textField_Angulo.setColumns(10);
        textField_Angulo.setBounds(210, 36, 50, 22);
        contentPane.add(textField_Angulo);
        
        textField_Robot = new JTextField();
        textField_Robot.setColumns(10);
        textField_Robot.setBounds(78, 59, 50, 22);
        contentPane.add(textField_Robot);
        
        textField_Raio = new JTextField();
        textField_Raio.setColumns(10);
        textField_Raio.setBounds(308, 36, 50, 22);
        contentPane.add(textField_Raio);
        
        JLabel btnLabel_Distancia = new JLabel("Distância ");
        btnLabel_Distancia.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnLabel_Distancia.setBounds(369, 30, 70, 32);
        contentPane.add(btnLabel_Distancia);
        
        JLabel btnLabel_Angulo = new JLabel("Ângulo");
        btnLabel_Angulo.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnLabel_Angulo.setBounds(166, 30, 70, 32);
        contentPane.add(btnLabel_Angulo);
        
        JLabel btnLabel_Raio = new JLabel("Raio");
        btnLabel_Raio.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnLabel_Raio.setBounds(277, 30, 34, 32);
        contentPane.add(btnLabel_Raio);
        
        JSpinner spinner = new JSpinner();
        spinner.setFont(new Font("Tahoma", Font.PLAIN, 13));
        spinner.setBounds(467, 191, 56, 32);
        contentPane.add(spinner);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(90, 246, 563, 95);
        contentPane.add(scrollPane);
        
        JTextArea textArea = new JTextArea();
        scrollPane.setViewportView(textArea);
        
        JLabel label_Consola = new JLabel("Consola:");
        label_Consola.setFont(new Font("Tahoma", Font.PLAIN, 13));
        label_Consola.setBounds(90, 221, 82, 16);
        contentPane.add(label_Consola);
        
        JLabel label_Numero = new JLabel("Número:");
        label_Numero.setFont(new Font("Tahoma", Font.PLAIN, 13));
        label_Numero.setBounds(415, 195, 65, 25);
        contentPane.add(label_Numero);

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
