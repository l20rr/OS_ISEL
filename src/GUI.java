import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Color;

public class GUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTextField textField_Distancia;
    private JTextField textField_Angulo;
    private JTextField textField_Raio;
    private JTextField textField_Robot;
    private JTextArea textArea_console;

    private Gravador gravador;
    private BaseDados db;
    private MovimentosAleatorios movimentoAleatorioAtivo;

    // ===== PARÂMETROS PRÓPRIOS DA GUI =====
    private int distancia = 20;
    private int angulo = 90;
    private int raio = 10;

    public void MyPrint(String msg) {
        SwingUtilities.invokeLater(() ->
                textArea_console.append(msg + "\n")
        );
    }

    public GUI(Application app) {
        this.db = app.getDB();
        this.gravador = db.getGravador();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 673, 644);

        // ================= CONTENT PANE =================
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        // ================= BOTÕES =================
        JButton btnFrente = new JButton("FRENTE");
        btnFrente.setBackground(Color.GREEN);
        btnFrente.setBounds(237, 108, 99, 32);
        btnFrente.addActionListener(e -> {
            Comando c = new Comando("RETA", distancia, 0);
            enviarComandoAsync(c);
            gravador.registarComando(c);
        });
        contentPane.add(btnFrente);

        JButton btnTras = new JButton("TRÁS");
        btnTras.setBackground(new Color(255, 128, 64));
        btnTras.setBounds(237, 171, 99, 32);
        btnTras.addActionListener(e -> {
            Comando c = new Comando("RETA", -distancia, 0);
            enviarComandoAsync(c);
            gravador.registarComando(c);
        });
        contentPane.add(btnTras);

        JButton btnParar = new JButton("PARAR");
        btnParar.setBackground(Color.RED);
        btnParar.setBounds(237, 140, 99, 32);
        btnParar.addActionListener(e -> {
            if (movimentoAleatorioAtivo != null) {
                movimentoAleatorioAtivo.bloquear();
                movimentoAleatorioAtivo = null;
                MyPrint("Geração de movimentos aleatórios parada.");
            }
            db.getServidor().pararForcado();
            MyPrint("Robô parado (forçado).");
            gravador.registarComando(new Comando("PARAR", 0, 0));
        });
        contentPane.add(btnParar);

        JButton btnDir = new JButton("DIREITA");
        btnDir.setBackground(Color.BLUE);
        btnDir.setBounds(335, 140, 99, 32);
        btnDir.addActionListener(e -> {
            Comando c = new Comando("CURVA_DIREITA", angulo, raio);
            enviarComandoAsync(c);
            gravador.registarComando(c);
        });
        contentPane.add(btnDir);

        JButton btnEsq = new JButton("ESQUERDA");
        btnEsq.setBackground(new Color(255, 128, 192));
        btnEsq.setBounds(139, 140, 99, 32);
        btnEsq.addActionListener(e -> {
            Comando c = new Comando("CURVA_ESQUERDA", angulo, raio);
            enviarComandoAsync(c);
            gravador.registarComando(c);
        });
        contentPane.add(btnEsq);

        // ================= CHECKBOX LIGAR =================
        JCheckBox checkLigar = new JCheckBox("Ligar");
        checkLigar.setBounds(36, 36, 97, 23);
        checkLigar.setBackground(Color.WHITE);
        checkLigar.setSelected(db.isRobotAberto());
        checkLigar.addActionListener(e -> {
            if (db.isRobotAberto()) {
                db.getServidor().closeEV3();
                db.setRobotAberto(false);
                textField_Robot.setText("");
                MyPrint("Servidor desligado.");
            } else {
                boolean ok = db.getServidor().openEV3(db.getNomeRobot());
                db.setRobotAberto(ok);
                if (ok) {
                    db.getServidor().desbloquear();
                    textField_Robot.setText(db.getNomeRobot());
                    MyPrint("Servidor ligado.");
                } else {
                    MyPrint("Erro ao ligar robô.");
                }
            }
            checkLigar.setSelected(db.isRobotAberto());
        });
        contentPane.add(checkLigar);

        // ================= CAMPOS =================
        JLabel lblDistancia = new JLabel("Distância");
        lblDistancia.setBounds(369, 30, 70, 32);
        contentPane.add(lblDistancia);

        textField_Distancia = new JTextField(String.valueOf(distancia));
        textField_Distancia.setBounds(430, 36, 50, 22);
        textField_Distancia.addActionListener(e -> {
            distancia = Integer.parseInt(textField_Distancia.getText());
            MyPrint("Distância alterada para: " + distancia);
        });
        contentPane.add(textField_Distancia);

        JLabel lblAngulo = new JLabel("Ângulo");
        lblAngulo.setBounds(166, 30, 70, 32);
        contentPane.add(lblAngulo);

        textField_Angulo = new JTextField(String.valueOf(angulo));
        textField_Angulo.setBounds(210, 36, 50, 22);
        textField_Angulo.addActionListener(e -> {
            angulo = Integer.parseInt(textField_Angulo.getText());
            MyPrint("Ângulo alterado para: " + angulo);
        });
        contentPane.add(textField_Angulo);

        JLabel lblRaio = new JLabel("Raio");
        lblRaio.setBounds(277, 30, 50, 32);
        contentPane.add(lblRaio);

        textField_Raio = new JTextField(String.valueOf(raio));
        textField_Raio.setBounds(308, 36, 50, 22);
        textField_Raio.addActionListener(e -> {
            raio = Integer.parseInt(textField_Raio.getText());
            MyPrint("Raio alterado para: " + raio);
        });
        contentPane.add(textField_Raio);

        JLabel lblRobot = new JLabel("ROBOT");
        lblRobot.setBounds(516, 37, 56, 16);
        contentPane.add(lblRobot);

        textField_Robot = new JTextField();
        textField_Robot.setBounds(575, 31, 50, 22);
        contentPane.add(textField_Robot);

        
        // ================= MOVIMENTOS ALEATÓRIOS =================
        JLabel lblNumero = new JLabel("Número:");
        lblNumero.setBounds(475, 195, 65, 25);
        contentPane.add(lblNumero);

        
        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 16, 1);
        JSpinner spinner = new JSpinner(model);
        spinner.setBounds(527, 191, 56, 32);
        contentPane.add(spinner);

        JRadioButton rdbtnMovAlt = new JRadioButton("Movimentos Aleatórios");
        rdbtnMovAlt.setBounds(460, 160, 210, 25);
        rdbtnMovAlt.setBackground(Color.WHITE);
        rdbtnMovAlt.addActionListener(e -> {
            if (!rdbtnMovAlt.isSelected()) {
                if (movimentoAleatorioAtivo != null) {
                    movimentoAleatorioAtivo.bloquear();
                    movimentoAleatorioAtivo = null;
                    MyPrint("Geração de movimentos aleatórios parada.");
                }
                return;
            }
            int qtd = (int) spinner.getValue();
            if (movimentoAleatorioAtivo == null) {
                movimentoAleatorioAtivo = new MovimentosAleatorios(db, qtd, this);
                movimentoAleatorioAtivo.start();
            }
            movimentoAleatorioAtivo.desbloquear();
            MyPrint("Gerando blocos de " + qtd + " movimentos aleatórios...");
        });
        contentPane.add(rdbtnMovAlt);
        // ================= CONSOLE =================
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(75, 290, 520, 160);
        contentPane.add(scrollPane);

        textArea_console = new JTextArea();
        scrollPane.setViewportView(textArea_console);

        JLabel lblConsole = new JLabel("Consola:");
        lblConsole.setBounds(65, 270, 82, 16);
        contentPane.add(lblConsole);

        // ================= WINDOW CLOSE =================
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (db.isRobotAberto()) {
                    db.getServidor().pararForcado();
                    db.getServidor().closeEV3();
                }
                db.setTerminar(true);
            }
        });

        setVisible(true);
    }

    // ================= MÉTODO CENTRAL =================
    private void enviarComandoAsync(Comando c) {
        new Thread(() -> {
            try {
                db.getServidor().inserirComandoAposBloco(c);
                MyPrint("Comando enviado: " + c);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
