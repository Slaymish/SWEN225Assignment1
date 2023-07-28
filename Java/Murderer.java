/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/


import java.util.*;

// line 60 "model.ump"
// line 226 "model.ump"
public class Murderer extends Game
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Murderer(int aCurrentPlayerTurn, Board aBoard, List<Card> aAllCards, Murderer aMurderer, Map<Int,Player> aPlayerMap)
  {
    super(aCurrentPlayerTurn, aBoard, aAllCards, aMurderer, aPlayerMap);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public void delete()
  {
    super.delete();
  }


  /**
   * Returns if the given cards are the murder ones
   */
  // line 76 "model.ump"
  public boolean checkMurderer(Card weapon, Card estate, Card person){
    
  }


  /**
   * Formatted display output
   */
  // line 83 "model.ump"
  public String toString(Card weapon, Card estate, Card person){
    
  }
  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 67 "model.ump"
  private Card weapon ;
// line 68 "model.ump"
  private Card estate ;
// line 69 "model.ump"
  private Card person ;

  
}