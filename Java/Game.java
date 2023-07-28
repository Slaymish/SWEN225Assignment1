/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/


import java.util.*;

// line 2 "model.ump"
// line 218 "model.ump"
public class Game
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Game Attributes
  private int currentPlayerTurn;
  private Board board;
  private List<Card> allCards;
  private Murderer murderer;
  private Map<Integer,Player> playerMap;

  //Game Associations
  private List<Board> boards;
  private List<Card> cards;
  private List<Player> players;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Game(int aCurrentPlayerTurn, Board aBoard, List<Card> aAllCards, Murderer aMurderer, Map<Integer,Player> aPlayerMap)
  {
    currentPlayerTurn = aCurrentPlayerTurn;
    board = aBoard;
    allCards = aAllCards;
    murderer = aMurderer;
    playerMap = aPlayerMap;
    boards = new ArrayList<Board>();
    cards = new ArrayList<Card>();
    players = new ArrayList<Player>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setCurrentPlayerTurn(int aCurrentPlayerTurn)
  {
    boolean wasSet = false;
    currentPlayerTurn = aCurrentPlayerTurn;
    wasSet = true;
    return wasSet;
  }

  public boolean setBoard(Board aBoard)
  {
    boolean wasSet = false;
    board = aBoard;
    wasSet = true;
    return wasSet;
  }

  public boolean setAllCards(List<Card> aAllCards)
  {
    boolean wasSet = false;
    allCards = aAllCards;
    wasSet = true;
    return wasSet;
  }

  public boolean setMurderer(Murderer aMurderer)
  {
    boolean wasSet = false;
    murderer = aMurderer;
    wasSet = true;
    return wasSet;
  }

  public boolean setPlayerMap(Map<Integer,Player> aPlayerMap)
  {
    boolean wasSet = false;
    playerMap = aPlayerMap;
    wasSet = true;
    return wasSet;
  }

  public int getCurrentPlayerTurn()
  {
    return currentPlayerTurn;
  }

  public Board getBoard()
  {
    return board;
  }

  public List<Card> getAllCards()
  {
    return allCards;
  }

  public Murderer getMurderer()
  {
    return murderer;
  }

  public Map<Integer,Player> getPlayerMap()
  {
    return playerMap;
  }
  /* Code from template association_GetMany */
  public Board getBoard(int index)
  {
    Board aBoard = boards.get(index);
    return aBoard;
  }

  public List<Board> getBoards()
  {
    List<Board> newBoards = Collections.unmodifiableList(boards);
    return newBoards;
  }

  public int numberOfBoards()
  {
    int number = boards.size();
    return number;
  }

  public boolean hasBoards()
  {
    boolean has = boards.size() > 0;
    return has;
  }

  public int indexOfBoard(Board aBoard)
  {
    int index = boards.indexOf(aBoard);
    return index;
  }
  /* Code from template association_GetMany */
  public Card getCard(int index)
  {
    Card aCard = cards.get(index);
    return aCard;
  }

  public List<Card> getCards()
  {
    List<Card> newCards = Collections.unmodifiableList(cards);
    return newCards;
  }

  public int numberOfCards()
  {
    int number = cards.size();
    return number;
  }

  public boolean hasCards()
  {
    boolean has = cards.size() > 0;
    return has;
  }

  public int indexOfCard(Card aCard)
  {
    int index = cards.indexOf(aCard);
    return index;
  }
  /* Code from template association_GetMany */
  public Player getPlayer(int index)
  {
    Player aPlayer = players.get(index);
    return aPlayer;
  }

  public List<Player> getPlayers()
  {
    List<Player> newPlayers = Collections.unmodifiableList(players);
    return newPlayers;
  }

  public int numberOfPlayers()
  {
    int number = players.size();
    return number;
  }

  public boolean hasPlayers()
  {
    boolean has = players.size() > 0;
    return has;
  }

  public int indexOfPlayer(Player aPlayer)
  {
    int index = players.indexOf(aPlayer);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfBoards()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addBoard(Board aBoard)
  {
    boolean wasAdded = false;
    if (boards.contains(aBoard)) { return false; }
    boards.add(aBoard);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeBoard(Board aBoard)
  {
    boolean wasRemoved = false;
    if (boards.contains(aBoard))
    {
      boards.remove(aBoard);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addBoardAt(Board aBoard, int index)
  {  
    boolean wasAdded = false;
    if(addBoard(aBoard))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfBoards()) { index = numberOfBoards() - 1; }
      boards.remove(aBoard);
      boards.add(index, aBoard);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveBoardAt(Board aBoard, int index)
  {
    boolean wasAdded = false;
    if(boards.contains(aBoard))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfBoards()) { index = numberOfBoards() - 1; }
      boards.remove(aBoard);
      boards.add(index, aBoard);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addBoardAt(aBoard, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCards()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addCard(Card aCard)
  {
    boolean wasAdded = false;
    if (cards.contains(aCard)) { return false; }
    cards.add(aCard);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCard(Card aCard)
  {
    boolean wasRemoved = false;
    if (cards.contains(aCard))
    {
      cards.remove(aCard);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCardAt(Card aCard, int index)
  {  
    boolean wasAdded = false;
    if(addCard(aCard))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCards()) { index = numberOfCards() - 1; }
      cards.remove(aCard);
      cards.add(index, aCard);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCardAt(Card aCard, int index)
  {
    boolean wasAdded = false;
    if(cards.contains(aCard))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCards()) { index = numberOfCards() - 1; }
      cards.remove(aCard);
      cards.add(index, aCard);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCardAt(aCard, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPlayers()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addPlayer(Player aPlayer)
  {
    boolean wasAdded = false;
    if (players.contains(aPlayer)) { return false; }
    players.add(aPlayer);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removePlayer(Player aPlayer)
  {
    boolean wasRemoved = false;
    if (players.contains(aPlayer))
    {
      players.remove(aPlayer);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addPlayerAt(Player aPlayer, int index)
  {  
    boolean wasAdded = false;
    if(addPlayer(aPlayer))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPlayers()) { index = numberOfPlayers() - 1; }
      players.remove(aPlayer);
      players.add(index, aPlayer);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMovePlayerAt(Player aPlayer, int index)
  {
    boolean wasAdded = false;
    if(players.contains(aPlayer))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPlayers()) { index = numberOfPlayers() - 1; }
      players.remove(aPlayer);
      players.add(index, aPlayer);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addPlayerAt(aPlayer, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    boards.clear();
    cards.clear();
    players.clear();
  }

  // line 12 "model.ump"
   private void Setup(){
    //board = new Board();
    
    allCards = CreateCards();  //randomize and create cards
    //distribute cards
    murderer = EstablishMurderer(allCards);  // set one card of each type as murder
  }

  // line 20 "model.ump"
   private void MainLoop(){
    //Display board state pre-input
    DisplayBoard();
    
    //turn stuff
    
    //input
    
    //response
  }


  /**
   * Creates and returns a list of all cards
   */
  // line 36 "model.ump"
   private List<Card> CreateCards(){

     return null;
   }


  /**
   * Picks a random card of each type to establish as 	the murderer
   */
  // line 42 "model.ump"
   private Murderer EstablishMurderer(List<Card> cards){

     return null;
   }


  /**
   * Gets and displays the board to the screen
   */
  // line 50 "model.ump"
   private void DisplayBoard(){
    
  }


  /**
   * returns the card class of a card by its name
   */
  // line 58 "model.ump"
   public Card getCardByName(String cardName){

     return null;
   }


  public String toString()
  {
    return super.toString() + "["+
            "currentPlayerTurn" + ":" + getCurrentPlayerTurn()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "board" + "=" + (getBoard() != null ? !getBoard().equals(this)  ? getBoard().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "allCards" + "=" + (getAllCards() != null ? !getAllCards().equals(this)  ? getAllCards().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "murderer" + "=" + (getMurderer() != null ? !getMurderer().equals(this)  ? getMurderer().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "playerMap" + "=" + (getPlayerMap() != null ? !getPlayerMap().equals(this)  ? getPlayerMap().toString().replaceAll("  ","    ") : "this" : "null");
  }
}