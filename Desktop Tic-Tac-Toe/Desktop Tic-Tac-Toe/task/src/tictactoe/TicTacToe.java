package tictactoe;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToe extends JFrame {
    private final JLabel LabelStatus  = new JLabel("Game is not started");
    final private JButton buttonReset = new JButton("Start");
    private final JButton[][] fields = new JButton[3][3];
    private boolean whoisp1 = true;
    private boolean whoisp2 = true;
    private boolean gameInProgress;
    final private JButton player1 = new JButton("Human");
    final private JButton player2 = new JButton("Human");

    private boolean turn;
    private final String[][] board = new String[3][3];
    short occupied;
    //boolean gameOn = false;

    final private JMenuBar menuBar = new JMenuBar();
    final private JMenu fileMenu = new JMenu("Game");

    final private JMenuItem menuItemhh = new JMenuItem("Human vs Human");
    final private JMenuItem menuItemhr = new JMenuItem("Human vs Robot");
    final private JMenuItem menuItemrh = new JMenuItem("Robot vs Human");
    final private JMenuItem menuItemrr = new JMenuItem("Robot vs Robot");
    final private JMenuItem menuItemExit = new JMenuItem("Exit");

    public TicTacToe() {
        super("Hello App");
        turn = true;
        this.occupied = 0;
        gameInProgress = false;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Tic Tac Toe");
        setResizable(false);
        setSize(450, 450);
        setVisible(true);
        setLayout(null);
        this.createMenu();

        JPanel bluePanel = new JPanel(new GridLayout(1, 3, 0, 0));
        bluePanel.setBounds(0,0,450,30);
        bluePanel.setBackground(Color.BLUE);
        bluePanel.setBorder(BorderFactory.createEmptyBorder());
        bluePanel.setVisible(true);
        add(bluePanel);

        JPanel greenPanel = new JPanel(new GridLayout(3, 3, 0, 0));
        greenPanel.setBounds(0,30,450,300);
        greenPanel.setBackground(Color.GREEN);
        greenPanel.setBorder(BorderFactory.createEmptyBorder());
        greenPanel.setVisible(true);
        add(greenPanel);


        JPanel redPanel = new JPanel();
        redPanel.setBounds(0,  330,450,120);
        redPanel.setLayout(new BorderLayout());
        redPanel.setBackground(Color.LIGHT_GRAY);
        redPanel.setVisible(true);

        for (int i = 2; i  >= 0; i--){
            for (int j = 0; j < 3; j++) {
                fields[i][j] = new JButton(" ");
                fields[i][j].setFocusPainted(false);
                fields[i][j].setMargin(new Insets(0, 0, 0, 0));
                fields[i][j].setName("Button"+ (char) ('A' + j) + (i + 1));
                greenPanel.add(fields[i][j]);
                int finalI = i;
                int finalJ = j;
                fields[i][j].setEnabled(false);
                fields[i][j].addActionListener(e -> {
                    if (" ".equals(fields[finalI][finalJ].getText())) {
                        fields[finalI][finalJ].setText(turn ? "X" : "O");

                        boolean terminated = checkstatus(finalI, finalJ);

                        if (terminated) {
                            turn = !turn;
                            LabelStatus.setText(String.format("The turn of %s Player (%s)",
                                    turn && whoisp1 || !turn && whoisp2 ? "Human" : "Robot",
                                    turn ? "X" : "O"));
                        }

                        if (gameInProgress &&
                                (turn && !whoisp1 || !turn && !whoisp2)) {
                            robotMove();
                        }
                    }
                });
            }
        }



        redPanel.setLayout(null);
        player1.setName("ButtonPlayer1");
        player2.setName("ButtonPlayer2");
        buttonReset.setBounds(250,25,100,50);
        buttonReset.setName("ButtonStartReset");
        bluePanel.add(player1);
        bluePanel.add(buttonReset);
        bluePanel.add(player2);

        LabelStatus.setBounds(20,20,200,20);
        LabelStatus.setName("LabelStatus");
        LabelStatus.setText("Game is not started");
        LabelStatus.setVisible(true);
        redPanel.add(LabelStatus);
        add(redPanel);

        buttonReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("Start".equals(buttonReset.getText())) {
                    startGame();
                } else if ("Reset".equals(buttonReset.getText())) {
                    buttonReset.setText("Start");
                    LabelStatus.setText("Game is not started");
                    gameInProgress = false;
                    player1.setEnabled(true);
                    player2.setEnabled(true);
                    menuItemhh.setEnabled(true);
                    menuItemhr.setEnabled(true);
                    menuItemrh.setEnabled(true);
                    menuItemrr.setEnabled(true);
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            fields[i][j].setText(" ");
                            board[i][j] = "";
                            fields[i][j].setEnabled(false);
                        }
                    }
                }




            }
        });

        player1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                whoisp1 = !whoisp1;
                player1.setText(whoisp1 ? "Human" : "Robot");
            }
        });


        player2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                whoisp2 = !whoisp2;
                player2.setText(whoisp2 ? "Human" : "Robot");
            }
        });

        setVisible(true);
    }



    private void createMenu() {
        fileMenu.setName("MenuGame");
        menuItemhh.setName("MenuHumanHuman");
        menuItemhr.setName("MenuHumanRobot");
        menuItemrh.setName("MenuRobotHuman");
        menuItemrr.setName("MenuRobotRobot");
        menuItemExit.setName("MenuExit");

        fileMenu.add(menuItemhh);                          // 8
        fileMenu.add(menuItemhr);                          // 8
        fileMenu.add(menuItemrh);                          // 8
        fileMenu.add(menuItemrr);                          // 8
        fileMenu.addSeparator();
        fileMenu.add(menuItemExit);

        menuBar.add(fileMenu);                              // 12
        setJMenuBar(menuBar);                               // 13

        // you can rewrite it with a lambda if you prefer this
        menuItemExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });


        menuItemhh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                whoisp1 = true;
                whoisp2 = true;
                player1.setText("Human");
                player2.setText("Human");
                startGame();

            }
        });
        menuItemhr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                whoisp1 = true;
                whoisp2 = false;
                player1.setText("Human");
                player2.setText("Robot");
                startGame();
            }
        });

        menuItemrh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                whoisp1 = false;
                whoisp2 = true;
                player1.setText("Robot");
                player2.setText("Human");
                startGame();

            }
        });

        menuItemrr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                whoisp1 = false;
                whoisp2 = false;
                player1.setText("Robot");
                player2.setText("Robot");
                startGame();
            }
        });
    }

    boolean robotMove() {
        boolean terminated = true;
        for (int i = 0; i < 3; i++) {
            outerloop:
            for (int j = 0; j < 3; j++) {
                if (" ".equals(fields[i][j].getText())) {
                    fields[i][j].setText(turn ? "X" : "O");
                    terminated = checkstatus(i, j);
                    turn = !turn;
                    if (terminated) {
                        LabelStatus.setText(String.format("The turn of %s Player (%s)",
                                turn && whoisp1 || !turn && whoisp2 ? "Human" : "Robot",
                                turn ? "X" : "O"));
                    }
                    i=3;j=3;
                    break outerloop;
                }
            }
        }
        if (terminated &&
                (turn && !whoisp1 || !turn && !whoisp2)) {
            robotMove();
        }
        return false;
    }


    boolean  checkstatus(int i, int j) {
        board[i][j] = (turn ? "X" : "O");
        occupied++;
        if (board[i][j].equals(board[(i + 1) % 3][j]) && board[i][j].equals(board[(i + 2) % 3][j])
                || board[i][j].equals(board[i][(j + 1) % 3]) && board[i][j].equals(board[i][(j + 2) % 3])
                || i == j &&  board[i][j].equals(board[(i + 1) % 3][(j + 1) % 3]) && board[i][j].equals(board[(i + 2) % 3][(j + 2) % 3])
                || i == (2-j) && board[i][j].equals(board[(i + 1) % 3][(j +2) % 3])
                && board[i][j].equals(board[(i + 2) % 3][(j +1) % 3])){
            this.LabelStatus.setText("The " + (turn && whoisp1 || !turn && whoisp2 ? "Human" : "Robot")
                    + " Player (" +board[i][j] + ") wins");
            gameInProgress = false;

            for (int m = 2; m  >= 0; m--) {
                for (int k = 0; k < 3; k++) {
                    fields[m][k].setEnabled(false);
                }
            }
            return false;
        }


        if (occupied == 9) {
            this.LabelStatus.setText("Draw");
            gameInProgress = false;

            for (int m = 2; m  >= 0; m--) {
                for (int k = 0; k < 3; k++) {
                    fields[m][k].setEnabled(false);
                }
            }
            return false;
        }
        return true;
    }

    private void startGame() {
        turn = true;
        occupied = 0;
        LabelStatus.setText(String.format("The turn of %s Player (X)",
                whoisp1 ? "Human" : "Robot"));
        gameInProgress = true;
        buttonReset.setText("Reset");
        player1.setEnabled(false);
        player2.setEnabled(false);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                fields[i][j].setText(" ");
                board[i][j] = "";
                fields[i][j].setEnabled(true);
            }
        }
        if (turn && !whoisp1 || !turn && !whoisp2) {
            robotMove();
        }
    }
}