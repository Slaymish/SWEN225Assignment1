/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/



// line 144 "model.ump"
// line 244 "model.ump"
public class Card
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Card Attributes
  private int cardType;
  private String cardName;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Card(int aCardType, String aCardName)
  {
    cardType = aCardType;
    cardName = aCardName;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setCardType(int aCardType)
  {
    boolean wasSet = false;
    cardType = aCardType;
    wasSet = true;
    return wasSet;
  }

  public boolean setCardName(String aCardName)
  {
    boolean wasSet = false;
    cardName = aCardName;
    wasSet = true;
    return wasSet;
  }

  public int getCardType()
  {
    return cardType;
  }

  public String getCardName()
  {
    return cardName;
  }

  public void delete()
  {}


  /**
   * Returns if cards are equal
   */
  // line 153 "model.ump"
   public bool equals(Card other){
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "cardType" + ":" + getCardType()+ "," +
            "cardName" + ":" + getCardName()+ "]";
  }
}