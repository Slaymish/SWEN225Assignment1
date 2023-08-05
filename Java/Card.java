public class Card
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Card Attributes
  enum CardType{Character, Weapon, Estate}
  CardType cardType;
  private final String cardName;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Card(CardType aCardType, String aCardName)
  {
    cardType = aCardType;
    cardName = aCardName;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public CardType getCardType()
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
