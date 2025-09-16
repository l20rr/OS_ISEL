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

public class GUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private BaseDados db;
    private JTextField out_textField;

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
        setBounds(100, 100, 450, 300);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Botão "Reta"
        JButton btnReta = new JButton("Reta");
        btnReta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (!Beans.isDesignTime() && db != null) {
                    db.getRobot().Reta(db.getDistancia());
                    db.getRobot().Parar(false);
                    MyPrint("fiz uma reta com " + db.getDistancia());
                }
            }
        });
        btnReta.setBounds(31, 113, 89, 23);
        contentPane.add(btnReta);

        // Botao On Off
        JRadioButton rdbtnOnOff = new JRadioButton("On/Off");
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
        rdbtnOnOff.setBounds(31, 30, 109, 23);
        contentPane.add(rdbtnOnOff);

        JLabel btnlabel = new JLabel("Distância ");
        btnlabel.setBounds(252, 33, 56, 16);
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
        out_textField.setBounds(297, 31, 116, 22);
        contentPane.add(out_textField);
        if (!Beans.isDesignTime() && db != null) {
            out_textField.setText("" + db.getDistancia());
        } else {
            out_textField.setText("0");
        }
        out_textField.setColumns(10);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(227, 113, 199, 91);
        contentPane.add(scrollPane);
        
        JTextArea textArea_console = new JTextArea();
        scrollPane.setViewportView(textArea_console);

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
