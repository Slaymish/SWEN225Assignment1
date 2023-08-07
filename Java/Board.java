
import java.util.*;
import java.util.stream.Collectors;

// line 93 "model.ump"
// line 231 "model.ump"
public class Board {

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    private List<Estate> estateList;

    private Map<Integer, Person> players;

    //The map is a 24x24 grid
    final int BoardHeight = 24;
    final int BoardWidth = 24;

    //2D array of Cell objects representing the board
    private final Cell[][] board; // [y][x]

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Board() {
        board = new Cell[BoardHeight][BoardWidth];
        players = new HashMap<>();
        estateList = new ArrayList<>();
        initializeBoard();
    }

    public List<Estate> getEstateList() {
        return estateList;
    }

    private void initializeBoard() { //empty board full of empty cells
        for (int i = 0; i < BoardHeight; i++) {
            for (int j = 0; j < BoardWidth; j++) {
                board[i][j] = new EmptyCell(); // Empty cell
            }
        }
        BuildWalls();
        BuildEstates();
        BuildDoors();
        System.out.println("BOARD INITIALIZED.");
    }



    /**
     * Convert playerMap to a map of Person objects and add them to the board
     *
     * @param playerMap
     */
    public void BuildPeople(HashMap<Integer, Player> playerMap) {
        players = playerMap.entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> new Person(e.getValue())));

        updatePeopleOnBoard();
    }

    /**
     * Goes through list of players and updates their position on the board
     */
    public void updatePeopleOnBoard() {
        players.forEach((playerNum, person) -> {
            board[person.getPrevY()][person.getPrevX()] = new EmptyCell(); // TODO change to previous cell (not just empty)
            board[person.getY()][person.getX()] = person;
        });
    }

    public String displayBoard() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < BoardHeight; i++) {
            for (int j = 0; j < BoardWidth; j++) {
                sb.append(board[i][j].getDisplayChar() + "|");
            }
            sb.append("\n");
        }

        //table of symbols
        sb.append("\nGLOSSARY OF SYMBOLS:"
                + "\n| W = WALL           |"
                + "\n| I = ESTATE WALL    |"
                + "\n| " + ConsoleCommands.inYellow("P") + " = PERSON         |"
                + "\n| " + ConsoleCommands.inPurple("D") + " = DOOR OF ESTATE |");

        System.out.println(sb);
        return sb.toString();
    }

    private void BuildWalls() {
        placeWall(5, 11);//north pillar
        placeWall(11, 5);//left pillar
        placeWall(24 - 7, 11);//south pillar
        placeWall(11, 24 - 7);//left pillar
    }

    private void placeWall(int row, int col) {
        if (row >= 0 && row + 1 < BoardHeight && col >= 0 && col + 1 < BoardWidth) {
            board[row][col] = new Wall();
            board[row][col + 1] = new Wall();
            board[row + 1][col] = new Wall();
            board[row + 1][col + 1] = new Wall();
        }
    }

    /**
     * Creates the estates and interiors
     * Add them to estateList
     */
    private void BuildEstates() {
        List<Estate> _estateList = List.of(
                new Estate("Haunted House") // Top Left
                        .setOrigin(2, 2)
                        .setWidthHeight(5, 5)
                        .buildInside(),
                new Estate("Manic Manor") // Top Right
                        .setOrigin(2, 24-7)
                        .setWidthHeight(5, 5)
                        .buildInside(),
                new Estate("Calamity Castle") // Bottom Left
                        .setOrigin(24-7, 2)
                        .setWidthHeight(5, 5)
                        .buildInside(),
                new Estate("Peril Palace") // Bottom Right
                        .setOrigin(24-7, 24-7)
                        .setWidthHeight(5, 5)
                        .buildInside(),
                new Estate("Visitation Villa") // Center
                        .setOrigin(10, 9)
                        .setWidthHeight(4, 6)
                        .buildInside()
        );

        estateList = _estateList;
        estateList.forEach(estate -> placeEstate(estate));
    }

    private void placeEstate(Estate estate) {
        estate.getEstateCells().forEach(insideCell -> {
            board[insideCell.getRow()][insideCell.getCol()] = insideCell;
        });
    }

    private void BuildDoors() {
        //Haunted House
        placeDoor(3, 6, estateList.get(0));
        placeDoor(6, 5, estateList.get(0));
        System.out.println(estateList.get(0).toString());
        //Manic Manor
        placeDoor(5, 24 - 7, estateList.get(1));
        placeDoor(6, 24 - 4, estateList.get(1));
        System.out.println(estateList.get(1).toString());
        //Calamity Castle
        placeDoor(24 - 7, 3, estateList.get(2));
        placeDoor(24 - 6, 6, estateList.get(2));
        System.out.println(estateList.get(2).toString());
        //Peril Palace
        placeDoor(24 - 7, 24 - 6, estateList.get(3));
        placeDoor(24 - 4, 24 - 7, estateList.get(3));
        System.out.println(estateList.get(3).toString());
        //Visitation Villa
        placeDoor(12, 9, estateList.get(4));
        placeDoor(10, 12, estateList.get(4));
        placeDoor(11, 14, estateList.get(4));
        placeDoor(13, 11, estateList.get(4));
        System.out.println(estateList.get(4).toString());
    }

    private void placeDoor(int row, int col, Estate estate) {
        if (row >= 0 && row < BoardHeight && col >= 0 && col < BoardWidth) {
            board[row][col] = new Door(estate); // Door cell associated with the estate
        }
    }

    /**
     * Checks if the move is valid
     *
     * @param playerNum
     * @param moveX
     * @param moveY
     * @return
     */
    public boolean isMoveValid(int playerNum, int moveX, int moveY) throws Door.DoorEnteredEvent {
        Person player = players.get(playerNum);
        int proposedX = player.getX() + moveX;
        int proposedY = player.getY() + moveY;

        if (proposedX < 0 || proposedX >= BoardHeight || proposedY < 0 || proposedY >= BoardWidth) {
            return false;
        }

        // Check if cell is a door
        if(board[proposedY][proposedX] instanceof Door){
            Door door = (Door) board[proposedY][proposedX];
            //throw door event
            throw new Door.DoorEnteredEvent(door.getEstate());
        }
        //flipped Y and X to accommodate row and col in other methods.
        return board[proposedY][proposedX].isWalkable();
    }


    private class EmptyCell implements Cell {
        public EmptyCell() {
        }

        @Override
        public String getDisplayChar() {
            return "_";
        }
    }

    private class Wall implements Cell {
        public Wall() {
        }

        @Override
        public boolean isWalkable() {
            return false;
        }

        @Override
        public String getDisplayChar() {
            return "W";
        }
    }

}
