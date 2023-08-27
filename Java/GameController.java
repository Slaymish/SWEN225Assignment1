import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.swing.JPanel;

public class GameController extends JPanel implements KeyListener {
    public GameController() {
        System.out.println("GameController created");
        addKeyListener(this);
        setFocusable(true);
    }

    public void keyPressed(KeyEvent e) {
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_N) {
            createNewGame();
        }
    }

    public void keyTyped(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {}



    /**
     * Performs dice roll.
     * Stored in field in model (Game)
     * Then updates the view based on new state
     */
    public static void rollDice() {
        System.out.println("Dice rolled");
        Game.getGameInstance().performDiceRoll();
        Game.getGameInstance().setGameState(Game.GameState.WaitingForPlayerToMove);
        updateView();
    }

    public static void guessButtonClicked() {
        System.out.println("Player guessed");
        // TODO: Implement guessing (in model)
        // Temporarily adds a guess to the info area
        GameView.getView().updateInfo("Player guessed");
        // and just testing the context buttons
        GameView.getView().setContextButtons("roll");
    }

    public static void solveButtonClicked() {
        System.out.println("Solve Button");
        // TODO: Implement solve (in model)
        // Temporarily adds a solve to the info area
        GameView.getView().updateInfo("Player solved");
        // and just testing the context buttons
        GameView.getView().setContextButtons("roll");
    }

    public static void createNewGame() {
        System.out.println("New Game");
        Game.getGameInstance().resetGame();
        updateView();
    }

    public static void quitGame() {
        System.out.println("Quit Game");
        System.exit(0);
    }

    /**
     * Method called when cell on GUI's board is clicked
     * Row/col are where u clicked
     * @param row
     * @param col
     */
    public static void cellClicked(int row, int col) {
        switch (Game.getState()){
            case WaitingForPlayerToMove -> {
                boolean canMoveThere = Game.getGameInstance().getBoard().isMoveValidAtClick(row,col);
                if(!canMoveThere) {
                    GameView.getView().updateInfo("Can't move there!");
                    return;
                }
                try {
                    Game.getGameInstance().moveCurrentPlayerTo(row,col);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Optional<Estate> inEstate = Game.getGameInstance().currentPlayerInEstate();
                if(inEstate.isPresent()){
                    Game.getGameInstance().setGameState(Game.GameState.PlayerCanGuessAndSolve);
                    System.out.println("You are in " + inEstate.get().name);
                }
                else{
                    Game.getGameInstance().setGameState(Game.GameState.PlayerCanSolve);
                    System.out.println("Not in an estate!");
                }

                GameView.getView().updateBoard();
                updateView();
            }
            default -> {
                GameView.getView().updateInfo("Not time to move!!");
            }
        }
    }

    /**
     * shortcuts
     */

    /**
     * Update the GUI's view based on the gameState
     */
    public static void updateView(){
        GameView view = GameView.getView();

        switch (Game.getState()){
            case PlayerToMove -> {
                view.updateBoard();
                view.updateInfo("Player To move..");
                view.setContextButtons("roll");
                updateToCurrentPlayer();
            }
            case WaitingForPlayerToMove -> {
                int[] maxMoves = Game.getGameInstance().getCurrentMaxMoves();
                view.updateInfo("You rolled a " + maxMoves[0] + " and a " + maxMoves[1]);
                view.setContextButtons(); // get rid of all buttons
                updateToCurrentPlayer();
            }
            case GameSetup -> {
                view.updateInfo("Game setting up...");
                view.setContextButtons();
                view.displaySetup();
            }
            case PlayerWon -> view.updateInfo("Player has won!");
            case PlayersLost -> view.updateInfo("You all lost!!");
            case PlayerCanGuessAndSolve -> {
                view.updateInfo("You can solve/guess nowww..");
                view.setContextButtons("guess","solve");
            }
            case PlayerCanSolve -> {
                view.updateInfo("You can solve nowww..");
                view.setContextButtons("solve");
            }
            default -> throw new IllegalStateException("This shouldn't happen!");
        }
    }


    private static void updateToCurrentPlayer(){
        String name = Game.getGameInstance().getCurrentPlayer().getName();
        GameView.getView().setBoardTitle("It is " + name + "'s turn");

        // Update the player's cards
        List<Card> playersCards = Game.getGameInstance().getCurrentPlayer().getCards();
        GameView.getView().showPlayersCards(playersCards);
    }

    /**
     * Method called by player num radio buttons
     * Sets up players (final step in game setup)
     * Set gamestate to PlayerToMove
     * @param i
     */
    public static void setupPlayers(int i) {
        System.out.println("Creating game with " + i + " players...");
        Game.getGameInstance().setupPlayers(i);
        Game.getGameInstance().setGameState(Game.GameState.PlayerToMove);
        updateView();
    }
}
