import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

public class GameView extends JFrame {
    private static GameView instance;
    private Game game;
    private JPanel boardPanel;
    private JPanel infoArea;
    private JTextArea infoAreaText;
    private Map<String,JButton> contextButtons = new HashMap<>();

    public static GameView getView() {
        if (instance == null) {
            instance = new GameView();
        }
        return instance;
    }

    private GameView() {
        super("Cluedo");

        try {
            // This is set so button.setBackground works correctly
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // Initialize components
        boardPanel = new JPanel(); // You can customize this to display the game board
        infoArea = new JPanel(); // You can use this to display player information or game messages
        infoAreaText = new JTextArea();

        // JMenu
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem NewGameItem = new JMenuItem("New Game");
        NewGameItem.addActionListener(e -> {
                   GameController.createNewGame();
                });
        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to quit the game?",
                    "Confirm Quit",
                    JOptionPane.YES_NO_OPTION
            );

            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        // Add a window listener to handle window close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(
                        GameView.this,
                        "Are you sure you want to quit the game?",
                        "Confirm Quit",
                        JOptionPane.YES_NO_OPTION
                );	

                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        menu.add(NewGameItem);
        menu.add(quitItem);

        menuBar.add(menu);
        setJMenuBar(menuBar);

        // Layout components
        setLayout(new BorderLayout());
        add(boardPanel, BorderLayout.WEST);
        add(infoArea, BorderLayout.EAST);

        // add context buttons
        contextButtons.put("roll", new JButton("Roll Dice"));
        contextButtons.put("guess", new JButton("Guess Murder"));
        contextButtons.put("solve", new JButton("Solve Attempt"));

        // Add action listeners (you'll need to define these in your controller)
        contextButtons.get("roll").addActionListener(e -> GameController.rollDice());
        contextButtons.get("guess").addActionListener(e -> GameController.guessButtonClicked());
        contextButtons.get("solve").addActionListener(e -> GameController.solveButtonClicked());


        JPanel contextPanel = new JPanel();

        for (JButton button : contextButtons.values()) {
            contextPanel.add(button);
        }

        // Add the roll button in the infoarea panel (use a subpanel to get the layout right)
        infoArea.setLayout(new BorderLayout());
        infoArea.add(infoAreaText, BorderLayout.CENTER);
        infoArea.add(contextPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Set the context of the game
     * "roll" = Roll Dice
     * "guess" = Guess Murder
     * "solve" = Solve Attempt
     * <p>
     * Clear current buttons, and display ones you've listed here
     * Uses varargs
     *
     * @param context
     * @return
     */
    public GameView setContextButtons(String... context) {
        contextButtons.values().stream().forEach(b -> {b.setVisible(false);});
       int size = context.length;
       if(size==0) return null;

       for (int i = 0; i<size;i++){
           JButton but = contextButtons.getOrDefault(context[i],null);
           if(but==null) throw new IllegalArgumentException("Button " + context[i] + " not found");
           but.setVisible(true);
       }
       return this;
    }




    // You can add methods to update the view based on changes in the game state

    /**
     * Update the boardPanel based on the current game state
     * (center panel)
     */
    public void updateBoard() {
        // Update the boardPanel based on the current game state
        if (Game.getState() == Game.GameState.GameSetup) {
            throw new IllegalStateException("Board should not be shown while still setting up");
        }

        boardPanel.removeAll();

        Cell[][] board = game.getBoard().getBoard();

        if (board==null) throw new NullPointerException("When trying to update board in view");

        JPanel buttonCellPanel = new JPanel();
        buttonCellPanel.setLayout(new GridLayout(board.length,board.length));

        //int offset = 20;
        for(int row = 0; row<board.length;row++){
            for(int col = 0; col < board[0].length;col++){
                Cell cell = board[row][col];
                JButton cellButton = new JButton(cell.getDisplayChar());
                cellButton.setMargin(new Insets(0, 0 ,0, 0));
                cellButton.setBackground(cell.getColor());
                cellButton.setForeground(Color.BLACK); // Setting the text color to black
                cellButton.setOpaque(true);
                int finalRow = row;
                int finalCol = col;
                cellButton.addActionListener(e -> GameController.cellClicked(finalRow, finalCol));
                buttonCellPanel.add(cellButton);
            }
        }

        buttonCellPanel.setPreferredSize(new Dimension(500,500));
        buttonCellPanel.setBackground(new Color(255,0,0));
        boardPanel.add(buttonCellPanel);

        boardPanel.revalidate();
        repaint();
    }

    /**
     * Update the infoArea based on the current game state
     * (right panel)
     */
    public void updateInfo(String info) {
        // Update the infoArea with new information
        infoAreaText.setText(info);
        repaint();
    }

    public GameView attachGame(Game game) {
        this.game = game;
        displaySetup();
        return this;
    }

    public void displaySetup() {
        boardPanel.removeAll();

        JPanel setupPanel = new JPanel();
        ButtonGroup playerNumRadioButtons = new ButtonGroup();
        JRadioButton threePlayers = new JRadioButton("3 Players");
        JRadioButton fourPlayers = new JRadioButton("4 Players");

        threePlayers.addActionListener(e -> GameController.setupPlayers(3));
        fourPlayers.addActionListener(e -> GameController.setupPlayers(4));

        playerNumRadioButtons.add(threePlayers);
        playerNumRadioButtons.add(fourPlayers);

        setupPanel.add(threePlayers);
        setupPanel.add(fourPlayers);

        boardPanel.add(setupPanel);
    }

}
