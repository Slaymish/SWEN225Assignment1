/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/



// line 156 "model.ump"
// line 249 "model.ump"
interface Cell
{
  //------------------------
  // INTERFACE
  //------------------------

  public default void delete()
  {}

  // line 160 "model.ump"
  default String toBoardString (){
    return "_";
  }


  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 161 "model.ump"
  default boolean isWalkable ()
  {
    return true;
  }

// line 164 "model.ump"
  default boolean isEntrance ()
  {
    return false;
  }

  
}