public class Main {
    public static void main(String[] args) {
        Game game = new Game(-1,new Board(null),null,new Murderer(-1,null,null,null,null),null);

        printGameState(game);
    }

    private static void printGameState(Game g){
        StringBuilder str = new StringBuilder();

        str.append(g.toString());
        str.append(g.getMurderer().toString());
        str.append(g.getPlayers().toString());

        System.out.println(str.toString());
    }
}
