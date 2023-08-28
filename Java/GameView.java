import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameView extends JFrame {
    private static GameView instance;
    private Game game;
    private JPanel boardPanel;

    private JPanel cardPanel;

    private JLabel boardTitle;
    private JPanel infoArea;
    private JTextArea infoAreaText;
    private Map<String,JButton> contextButtons = new HashMap<>();

    public static GameView getView() {
        if (instance == null) {
            instance = new GameView();
        }
        return instance;
    }

    public Game getGame() {
        return game;
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
        setSize(1000, 600);

        // Initialize components
        boardPanel = new JPanel(){
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                updateBoard(g);
            }
        }; // You can customize this to display the game board
        infoArea = new JPanel(); // You can use this to display player information or game messages
        infoAreaText = new JTextArea();
        cardPanel = new JPanel();

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
                } else {
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Prevent window from closing
                }
            }
        });



        GameController gameController = GameController.getController();
        this.getContentPane().addKeyListener(gameController);

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
        contextButtons.put("end turn", new JButton("End Turn"));

        // Add action listeners
        contextButtons.get("roll").addActionListener(e -> GameController.rollDice());
        contextButtons.get("guess").addActionListener(e -> GameController.guessButtonClicked());
        contextButtons.get("solve").addActionListener(e -> GameController.solveButtonClicked());
        contextButtons.get("end turn").addActionListener(e -> GameController.endTurnClicked());

        JPanel contextPanel = new JPanel();

        for (JButton button : contextButtons.values()) {
            contextPanel.add(button);
        }

        // Add the roll button in the infoarea panel (use a subpanel to get the layout right)
        infoArea.setLayout(new BorderLayout());
        infoArea.add(infoAreaText, BorderLayout.CENTER);
        infoArea.add(contextPanel, BorderLayout.SOUTH);

        // add title to boardpanel
        boardTitle = new JLabel();
        boardPanel.setLayout(new BorderLayout());
        boardPanel.add(boardTitle, BorderLayout.NORTH);
        boardPanel.add(cardPanel, BorderLayout.SOUTH);


        // Add the mouse listener to the boardPanel
        boardPanel.addMouseListener(gameController);

        setVisible(true);
    }
    public static void quitGame(){
        System.out.println("Not IOM");
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Sets the board title.
     * @param title
     */
    public void setBoardTitle(String title){
        boardTitle.setText(title);
        boardPanel.revalidate();
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
           
           // Set tooltips based on the context
           switch (context[i]) {
               case "roll":
                   but.setToolTipText("Click to roll two six sided die.");
                   break;
               case "guess":
                   but.setToolTipText("Click to make a guess about the murder.");
                   break;
               case "solve":
                   but.setToolTipText("Click to attempt to solve the mystery.");
                   //Override, disables solve button if current player has already tried to solve
                   but.setEnabled(!Game.getGameInstance().getCurrentPlayer().getHasGuessed());
                   break;
               // Add more cases and tooltips for other buttons as needed
           }
       }

       return this;
    }

    // You can add methods to update the view based on changes in the game state

    /**
     * Update the boardPanel based on the current game state
     * (center panel)
     */
    public void updateBoard(Graphics g) {
        // Update the boardPanel based on the current game state
        if (Game.getState() == Game.GameState.GameSetup) {return;}

        boardPanel.removeAll();

        Cell[][] board = Game.getGameInstance().getBoard().getBoard();

        if (board==null) throw new NullPointerException("When trying to update board in view");

        JPanel buttonCellPanel = new JPanel();
        buttonCellPanel.setLayout(new GridLayout(board.length,board.length));

        int offset = 20;
        for(int row = 0; row<board.length;row++){
            for(int col = 0; col < board[0].length;col++){
                // TODO: use mouselistener in controller
                Cell cell = board[row][col];
                g.setColor(cell.getColor());
                g.fillRect(col*offset,row*offset,offset,offset);
            }
        }

        buttonCellPanel.setPreferredSize(new Dimension(500,500));
        boardPanel.add(buttonCellPanel,BorderLayout.CENTER);
        boardPanel.add(boardTitle,BorderLayout.NORTH);

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

    public void showPlayersCards(List<Card> playersCards) {
        cardPanel = new JPanel();
        cardPanel.setLayout(new GridLayout(1, playersCards.size()));
        for (Card card : playersCards) {
            cardPanel.add(new JLabel(card.getCardName()));
        }
        boardPanel.add(cardPanel, BorderLayout.SOUTH);
        boardPanel.revalidate();
    }

    public void repaintBoard() {
        boardPanel.repaint();
    }
}
