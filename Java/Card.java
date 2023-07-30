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

  public int getCardType()
  {
    return cardType;
  }
  public String getCardName()
  {
    return cardName;
  }


  /**
   * Returns if cards are equal
   */
   public boolean equals(Card other){
     return (this.getCardType() == other.getCardType())
             && this.getCardName().equals( other.getCardName() );
   }


  public String toString()
  {
    return this.getCardType() + ": " + this.getCardName();
  }
}