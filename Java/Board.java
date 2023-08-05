
import java.util.*;
import java.util.stream.Collectors;

// line 93 "model.ump"
// line 231 "model.ump"
public class Board
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------
  
  private List<Estate> estateList = new ArrayList<Estate>();

    //Board Attributes
    private List<Cell> cellData;

    //Board Associations
    private List<Cell> cells;
    private List<Estate> estates;

    private Map<Integer,Person> players;
	
  //The map is a 24x24 grid
    final int BoardHeight = 24;
    final int BoardWidth = 24;
  
  //2D array of Cell objects representing the board
    private final Cell[][] board; // [y][x]

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Board()
  { 	
	  board = new Cell[BoardHeight][BoardWidth];
      players = new HashMap<Integer,Person>();
      initializeBoard();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template attribute_SetMany */
  public boolean addCellData(Cell aCellData)
  {
    boolean wasAdded = false;
    wasAdded = cellData.add(aCellData);
    return wasAdded;
  }

  public boolean removeCellData(Cell aCellData)
  {
    boolean wasRemoved = false;
    wasRemoved = cellData.remove(aCellData);
    return wasRemoved;
  }

  public boolean setEstateList(List<Estate> aEstateList)
  {
    boolean wasSet = false;
    estateList = aEstateList;
    wasSet = true;
    return wasSet;
  }
  /* Code from template attribute_GetMany */
  public Cell getCellData(int index)
  {
    Cell aCellData = cellData.get(index);
    return aCellData;
  }

  public Cell[] getCellData()
  {
    Cell[] newCellData = cellData.toArray(new Cell[cellData.size()]);
    return newCellData;
  }

  public int numberOfCellData()
  {
    int number = cellData.size();
    return number;
  }

  public boolean hasCellData()
  {
    boolean has = cellData.size() > 0;
    return has;
  }

  public int indexOfCellData(Cell aCellData)
  {
    int index = cellData.indexOf(aCellData);
    return index;
  }

  public List<Estate> getEstateList()
  {
    return estateList;
  }
  /* Code from template association_GetMany */
  public Cell getCell(int index)
  {
    Cell aCell = cells.get(index);
    return aCell;
  }

  public List<Cell> getCells()
  {
    List<Cell> newCells = Collections.unmodifiableList(cells);
    return newCells;
  }

  public int numberOfCells()
  {
    int number = cells.size();
    return number;
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


//------------------------
  // INTERFACE
  //------------------------

    /**
     * Convert playerMap to a map of Person objects and add them to the board
     * @param playerMap
     */
    public void BuildPeople(HashMap<Integer, Player> playerMap){
        players = playerMap.entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> new Person(e.getValue())));

        updatePeopleOnBoard();
    }

    /**
     * Goes through list of players and updates their position on the board
     */
    public void updatePeopleOnBoard(){
       players.forEach((playerNum,person) -> {
           board[person.getPrevY()][person.getPrevX()] = new EmptyCell(); // TODO change to previous cell (not just empty)
           board[person.getY()][person.getX()] = person;
       });
    }
  
  public String displayBoard() {
    StringBuilder sb = new StringBuilder();
	  for (int i = 0; i < BoardHeight; i++) {
          for (int j = 0; j < BoardWidth; j++) {
                sb.append(board[i][j].getDisplayChar()+"|");
          }
          sb.append("\n");
      }
	  
	  //table of symbols
	  sb.append("\nGLOSSARY OF SYMBOLS:"
	  		+ "\n| W = WALL           |"
	  		+ "\n| I = ESTATE WALL    |"
	  		+ "\n| P = PERSON         |"
	  		+ "\n| D = DOOR OF ESTATE |");

        System.out.println(sb);
        return sb.toString();
  }
	  
	private void BuildWalls() {
		placeWall(5, 11);//north pillar
		placeWall(11, 5);//left pillar
		placeWall(24-7, 11);//south pillar
		placeWall(11, 24-7);//left pillar
	}
	
	private void placeWall(int row, int col) {
	    if (row >= 0 && row + 1 < BoardHeight && col >= 0 && col + 1 < BoardWidth) {
	        board[row][col] = new Wall();
	        board[row][col + 1] = new Wall();
	        board[row + 1][col] = new Wall();
	        board[row + 1][col + 1] = new Wall();
	    }
	}
   
	private void BuildEstates() {
		placeEstate(2,2,5,5);//Top-Left ; Haunted House
		placeEstate(2,24-7,5,5);//Top-Right ; Manic Manor
		placeEstate(24-7,2,5,5);//Bottom-Left ; Calamity Castle
		placeEstate(24-7,24-7,5,5);//Bottom-Right ; Peril Palace
		placeEstate(10,9,4,6);// Center ; Visitation Villa
		
		estateList.add(0, new Estate("Haunted House"));
		estateList.add(1, new Estate("Manic Manor"));
		estateList.add(2, new Estate("Calamity Castle"));
		estateList.add(3, new Estate("Peril Palace"));
		estateList.add(4, new Estate("Visitation Villa"));
	}
	
	private void placeEstate(int row, int col, int height, int width) {
		if (row >= 0 && row + height <= BoardHeight && col >= 0 && col + width <= BoardWidth) {
    	  for (int i = row; i < row + height; i++) {
    		  for (int j = col; j < col + width; j++) {
    			  board[i][j] = new Inside(); // Estate cell
    		  }
    	  }
      }
	}
	
	private void BuildDoors() {
		//Haunted House
        placeDoor(3, 6, estateList.get(0));
        placeDoor(6, 5, estateList.get(0));
        System.out.println(estateList.get(0).toString());
        //Manic Manor
        placeDoor(5, 24-7, estateList.get(1));
        placeDoor(6, 24-4, estateList.get(1));
        System.out.println(estateList.get(1).toString());
        //Calamity Castle
        placeDoor(24-7, 3, estateList.get(2));
        placeDoor(24-6, 6, estateList.get(2));
        System.out.println(estateList.get(2).toString());
        //Peril Palace
        placeDoor(24-7, 24-6, estateList.get(3));
        placeDoor(24-4, 24-7, estateList.get(3));
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
     * @param playerNum
     * @param moveX
     * @param moveY
     * @return
     */
    public boolean isMoveValid(int playerNum, int moveX, int moveY){
        Person player = players.get(playerNum);
        int proposedX = player.getX() + moveX;
        int proposedY = player.getY() + moveY;

        if(proposedX < 0 || proposedX >= BoardHeight || proposedY < 0 || proposedY >= BoardWidth){
            return false;
        }

        return board[proposedX][proposedY].isWalkable();
    }


    private class EmptyCell implements Cell {
		public EmptyCell() {}
		
		@Override
		public String getDisplayChar() {return "_";}
    }
   
   private class Wall implements Cell {
	    public Wall() {}
	   
	    @Override
	    public boolean isWalkable()  {return false;}
	   
	    @Override
	    public String getDisplayChar() {return "W";}
   }

}
