import java.util.ArrayList;
import java.util.List;

public class Player {

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Player Attributes
  private String name;
  private boolean hasGuessed = false;
  private List<Card> cards;

  int[] position = new int[2];

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

  public boolean setPosition(int x, int y){
    boolean wasSet = false;
    position[0] = x;
    position[1] = y;
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

  public int[] getPosition(){
    return position;
  }

    public void move(int[] moveOffset){
        position[0] += moveOffset[0];
        position[1] += moveOffset[1];
        //updatePositionOnBoard();
    }
}
