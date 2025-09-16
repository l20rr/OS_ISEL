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
    private JTextField out_textField;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;

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

        JLabel btnlabel = new JLabel("ROBOT");
        btnlabel.setBounds(36, 61, 56, 16);
        contentPane.add(btnlabel);

        out_textField = new JTextField();
        out_textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!Beans.isDesignTime() && db != null) {
                    db.setDistancia(Integer.parseInt(out_textField.getText()));
                    MyPrint("a distancia foi alterada para : " + db.getDistancia());
                }
            }
        });
        out_textField.setBounds(430, 36, 50, 22);
        contentPane.add(out_textField);
        if (!Beans.isDesignTime() && db != null) {
            out_textField.setText("" + db.getDistancia());
        } else {
            out_textField.setText("0");
        }
        out_textField.setColumns(10);
        
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
        rdbtnNewRadioButton.setBounds(533, 150, 149, 40);
        contentPane.add(rdbtnNewRadioButton);
        
        textField = new JTextField();
        textField.setColumns(10);
        textField.setBounds(210, 36, 50, 22);
        contentPane.add(textField);
        
        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(78, 59, 50, 22);
        contentPane.add(textField_1);
        
        textField_2 = new JTextField();
        textField_2.setColumns(10);
        textField_2.setBounds(308, 36, 50, 22);
        contentPane.add(textField_2);
        
        JLabel btnlabel_1 = new JLabel("Distância ");
        btnlabel_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnlabel_1.setBounds(369, 30, 70, 32);
        contentPane.add(btnlabel_1);
        
        JLabel btnlabel_1_1 = new JLabel("Ângulo");
        btnlabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnlabel_1_1.setBounds(166, 30, 70, 32);
        contentPane.add(btnlabel_1_1);
        
        JLabel btnlabel_1_1_1 = new JLabel("Raio");
        btnlabel_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnlabel_1_1_1.setBounds(277, 30, 34, 32);
        contentPane.add(btnlabel_1_1_1);
        
        JSpinner spinner = new JSpinner();
        spinner.setFont(new Font("Tahoma", Font.PLAIN, 13));
        spinner.setBounds(471, 154, 56, 32);
        contentPane.add(spinner);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(90, 246, 563, 95);
        contentPane.add(scrollPane);
        
        JTextArea textArea = new JTextArea();
        scrollPane.setViewportView(textArea);

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