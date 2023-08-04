
import java.util.*;

// line 93 "model.ump"
// line 231 "model.ump"
public class Board
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------
  
  private List<Estate> estateList = new ArrayList<Estate>();
	
  //The map is a 24x24 grid
  final int BoardHeight = 24;
  final int BoardWidth = 24;
  
  //2D array of Cell objects representing the board
  private Cell[][] board;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Board()
  { 	
	board = new Cell[BoardHeight][BoardWidth];
    initializeBoard();
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
  
  
  public void displayBoard() {
	  for (int i = 0; i < BoardHeight; i++) {
          for (int j = 0; j < BoardWidth; j++) {
              System.out.print(board[i][j].getDisplayChar()+"|");
          }
          System.out.println();
      }
	  
	  //table of symbols
	  System.out.println("\nGLOSSARY OF SYMBOLS:"
	  		+ "\n| W = WALL           |"
	  		+ "\n| I = ESTATE WALL    |"
	  		+ "\n| P = PERSON         |"
	  		+ "\n| D = DOOR OF ESTATE |");

	  System.out.println("\nBOARD DISPLAYED.");
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