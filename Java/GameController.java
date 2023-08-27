import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Optional;

public class GameController {
    public GameController() {
        System.out.println("GameController created");
    }

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
        // TODO Implement new game (in model)
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
                if(canMoveThere){
                    System.out.println("Move player to " + Board.getBoard()[row][col]);
                    try {
                        Game.getGameInstance().moveCurrentPlayerTo(row,col);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    Optional<Estate> inEstate = Game.getGameInstance().currentPlayerInEstate();
                    if(inEstate.isPresent()){
                        Game.getGameInstance().setGameState(Game.GameState.PlayerToAttempt);
                        System.out.println("You are in " + inEstate.get().name);
                    }
                    else{
                        System.out.println("Not in an estate!");
                    }

                    updateView();

                }
                else{
                    GameView.getView().updateInfo("Can't move there!");
                }
            }
            default -> throw new RuntimeException("Can only click on cells when players turn to move");
        }
    }

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
                updatePlayerInTitle();
            }
            case WaitingForPlayerToMove -> {
                int[] maxMoves = Game.getGameInstance().getCurrentMaxMoves();
                view.updateInfo("You rolled a " + maxMoves[0] + " and a " + maxMoves[1]);
                view.setContextButtons(); // get rid of all buttons
                updatePlayerInTitle();
            }
            case GameSetup -> {
                view.updateInfo("Game setting up...");
                view.setContextButtons();
                view.displaySetup();
            }
            case PlayerWon -> view.updateInfo("Player has won!");
            case PlayersLost -> view.updateInfo("You all lost!!");
            case PlayerToAttempt -> {
                view.updateInfo("You can solve/guess nowww..");
                view.setContextButtons("guess","solve"); // TODO: Change if can only solve (if not in estate)
            }
            default -> throw new IllegalStateException("This shouldn't happen!");
        }
    }


    private static void updatePlayerInTitle(){
        String name = Game.getGameInstance().getCurrentPlayer().getName();
        GameView.getView().setTitle(name);
        // TODO instead of editing the title, add in another label and set that
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
