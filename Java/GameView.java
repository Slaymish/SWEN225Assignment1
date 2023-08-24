import javax.swing.*;
import java.awt.*;
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
            GameController.quitGame();
        });

        menu.add(NewGameItem);
        menu.add(quitItem);

        menuBar.add(menu);
        setJMenuBar(menuBar);


        // Layout components
        setLayout(new BorderLayout());
        add(boardPanel, BorderLayout.CENTER);
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
        // TODO: Display board in GUI (get from model)
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
        return this;
    }
}
