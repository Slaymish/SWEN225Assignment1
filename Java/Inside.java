/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/


// line 169 "model.ump"
// line 254 "model.ump"
public class Inside implements Cell {

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Inside()
  {
    super();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public void delete()
  {
    Cell.super.delete();
  }

  // line 174 "model.ump"
   public String toString(){
    return "Inside";
  }


  // line 176 "model.ump"
   public boolean isWalkable(){
    return false;
  }

@Override
public String getDisplayChar() {
	// TODO Auto-generated method stub
	return "I";
}

}