public class Main {
    public static void main(String[] args) {
        // Create view, attach game to view, and run view
        GameView view = GameView.getView()
                .attachGame(new Game())
                .setContextButtons("guess", "solve");

    }
}
