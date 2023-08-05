/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/


// line 194 "model.ump"
// line 265 "model.ump"
public class Person implements Cell {

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Person()
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
	// TODO Auto-generated method stub
	return "P";
}

}