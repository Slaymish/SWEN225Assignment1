/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/



// line 205 "model.ump"
// line 270 "model.ump"
public class Player
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Player Attributes
  private String name;
  private boolean hasGuessed;
  private List<Card> cards;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Player(String aName, boolean aHasGuessed, List<Card> aCards)
  {
    name = aName;
    hasGuessed = aHasGuessed;
    cards = aCards;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setHasGuessed(boolean aHasGuessed)
  {
    boolean wasSet = false;
    hasGuessed = aHasGuessed;
    wasSet = true;
    return wasSet;
  }

  public boolean setCards(List<Card> aCards)
  {
    boolean wasSet = false;
    cards = aCards;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }

  public boolean getHasGuessed()
  {
    return hasGuessed;
  }

  public List<Card> getCards()
  {
    return cards;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "hasGuessed" + ":" + getHasGuessed()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "cards" + "=" + (getCards() != null ? !getCards().equals(this)  ? getCards().toString().replaceAll("  ","    ") : "this" : "null");
  }
}