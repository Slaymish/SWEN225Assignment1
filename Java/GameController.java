import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameController implements KeyListener{
    public GameController() {
        System.out.println("GameController created");
    }

    public static void rollDice() {
        System.out.println("Dice rolled");
        // TODO: Implement dice rolling (in model)
        // Temporarily adds a dice roll to the info area
        GameView.getView().updateInfo("Dice rolled");
        // and just testing the context buttons
        GameView.getView().setContextButtons("guess", "solve");
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
    }

    public static void quitGame() {
        System.out.println("Quit Game");
        System.exit(0);
    }

    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {

    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }
}
