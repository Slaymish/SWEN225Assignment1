/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/


// line 194 "model.ump"
// line 265 "model.ump"
public class Person implements Cell {

  //------------------------
  // MEMBER VARIABLES
  //------------------------
    private final String name;
    private final Player player;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Person(Player player)
  {
    super();
    name = player.getName();
    this.player = player;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public void delete()
  {
    Cell.super.delete();
  }

  // line 199 "model.ump"
   public String toString(){
    return "Person";
  }

    // line 201 "model.ump"
   public boolean isWalkable(){
        return false;
    }

    @Override
    public String getDisplayChar() {
      return ConsoleCommands.inYellow(name.substring(0, 1));
    }

    public Player getPlayer() {
        return player;
    }

    public int getX() {
        return player.position[0];
    }

    public int getY() {
        return player.position[1];
    }

    public int getPrevX() {
        return player.prevPosition[0];
    }

    public int getPrevY() {
        return player.prevPosition[1];
    }
}