public class Main {
    public static void main(String[] args) {
        // Create view, attach game to view, and run view
        Game game = new Game();
        game.Setup();

        GameView view = GameView.getView()
                .attachGame(game)
                .setContextButtons("guess", "solve"); // what buttons to start showing

    }
}
