import java.util.ArrayList;
import java.util.List;

public class Player
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Player Attributes
  private String name;
  private boolean hasGuessed = false;
  private List<Card> cards;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Player(String aName)
  {
    name = aName;
    cards = new ArrayList<Card>();
  }

  //------------------------
  // INTERFACE
  //------------------------

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

  public void addCard(Card card)
  {
    cards.add(card);
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

  public String toString()
  {
    return this.name + System.getProperties().getProperty("line.separator") +
            "  " + "Your Cards" + "=" + getCards().toString() + System.getProperties().getProperty("line.separator");

  }
}
