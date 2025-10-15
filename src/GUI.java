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

    private JTextField textField_Distancia;
    private JTextField textField_Angulo;
    private JTextField textField_Robot;
    private JTextField textField_Raio;
    private JTextArea textArea_console;

    private BaseDados db;   
    private MovimentosAleatorios movimentoAleatorioAtivo = null;

    private void MyPrint(String msg) {
        SwingUtilities.invokeLater(() -> textArea_console.append(msg + "\n"));
    }

    public GUI(Application app) {
        db = app.getDB();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 702, 420);

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
            db.getServidor().Reta(db.getUltimaDistancia());
            MyPrint("fiz uma reta com " + db.getUltimaDistancia());
        });
        contentPane.add(btnFrente);

        JButton btnTras = new JButton("TRÁS");
        btnTras.setBackground(new Color(255, 128, 64));
        btnTras.setBounds(237, 171, 99, 32);
        btnTras.addActionListener(e -> {
            db.getServidor().Reta(-db.getUltimaDistancia());
            MyPrint("fiz uma marcha trás com " + db.getUltimaDistancia());
        });
        contentPane.add(btnTras);

        JButton btnParar = new JButton("PARAR");
        btnParar.setBackground(Color.RED);
        btnParar.setBounds(237, 140, 99, 32);
        btnParar.addActionListener(e -> {
            // 1️⃣ Para o servidor e limpa o buffer
            db.getServidor().Parar(true); 

            // 2️⃣ Para os movimentos aleatórios se estiverem ativos
            if (movimentoAleatorioAtivo != null) {
                movimentoAleatorioAtivo.pararMovimentos(); // método que deve parar a thread
                movimentoAleatorioAtivo = null;
                MyPrint("Movimentos aleatórios parados.");
            }

            // 3️⃣ Reativa o servidor para aceitar novos comandos
            db.getServidor().setAtiva(true);

            MyPrint("Robot parou e servidor reativado.");
        });

        contentPane.add(btnParar);

        JButton btnDir = new JButton("DIREITA");
        btnDir.setBackground(Color.BLUE);
        btnDir.setBounds(335, 140, 99, 32);
        btnDir.addActionListener(e -> {
            db.getServidor().CurvarDireita(db.getUltimoRaio(), db.getUltimoAngulo());
            MyPrint("robot fez uma curva direita com ângulo " + db.getUltimoAngulo() + " e raio " + db.getUltimoRaio());
        });
        contentPane.add(btnDir);

        JButton btnEsq = new JButton("ESQUERDA");
        btnEsq.setBackground(new Color(255, 128, 192));
        btnEsq.setBounds(139, 140, 99, 32);
        btnEsq.addActionListener(e -> {
            db.getServidor().CurvarEsquerda(db.getUltimoRaio(), db.getUltimoAngulo());
            MyPrint("robot fez uma curva esquerda com ângulo " + db.getUltimoAngulo() + " e raio " + db.getUltimoRaio());
        });
        contentPane.add(btnEsq);

        // ------------------- CHECKBOX LIGAR ----------------------
        JCheckBox checkLigar = new JCheckBox("Ligar");
        checkLigar.setBounds(36, 36, 97, 23);
        checkLigar.setBackground(Color.WHITE);
        checkLigar.setSelected(db.isRobotAberto());
        checkLigar.addActionListener(e -> {
            if (db.isRobotAberto()) {
                db.getServidor().CloseEV3();
                db.setRobotAberto(false);
                textField_Robot.setText("");
            } else {
                boolean aberto = db.getServidor().OpenEV3(db.getNomeRobot());
                db.setRobotAberto(aberto);
                textField_Robot.setText(aberto ? db.getNomeRobot() : "");
            }
            checkLigar.setSelected(db.isRobotAberto());
            MyPrint("o robo foi " + (db.isRobotAberto() ? "aberto" : "fechado"));
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
        lblNumero.setBounds(415, 195, 65, 25);
        contentPane.add(lblNumero);

        JSpinner spinner = new JSpinner();
        spinner.setFont(new Font("Tahoma", Font.PLAIN, 13));
        spinner.setBounds(467, 191, 56, 32);
        contentPane.add(spinner);

        JRadioButton rdbtnMovAlt = new JRadioButton("Movimentos Aleatórios");
        rdbtnMovAlt.setBackground(Color.WHITE);
        rdbtnMovAlt.setBounds(529, 198, 180, 23);
        contentPane.add(rdbtnMovAlt);

        rdbtnMovAlt.addActionListener(e -> {
            if (!rdbtnMovAlt.isSelected()) {
                if (movimentoAleatorioAtivo != null) {
                    movimentoAleatorioAtivo.pararMovimentos();
                    movimentoAleatorioAtivo = null;
                }
                db.getServidor().setAtiva(true);
                MyPrint("Movimentos aleatórios parados.");
                return;
            }
            int qtd = (int) spinner.getValue();
            MyPrint("Gerando " + qtd + " movimentos aleatórios...");
            movimentoAleatorioAtivo = new MovimentosAleatorios(db, qtd);
            movimentoAleatorioAtivo.start();
        });

        // ------------------- CONSOLE ----------------------
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(90, 246, 563, 95);
        contentPane.add(scrollPane);

        textArea_console = new JTextArea();
        scrollPane.setViewportView(textArea_console);

        JLabel lblConsole = new JLabel("Consola:");
        lblConsole.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblConsole.setBounds(90, 221, 82, 16);
        contentPane.add(lblConsole);

        // ------------------- WINDOW CLOSE ----------------------
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (db.isRobotAberto()) {
                    db.getServidor().CloseEV3();
                }
                if (movimentoAleatorioAtivo != null) {
                    movimentoAleatorioAtivo.pararMovimentos();
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
