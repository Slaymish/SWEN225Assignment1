import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Game
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Game Attributes
  private  int playerNum = 4;
  private int currentPlayerTurn = 0;
  private Board board;
  private List<Card> allCards;
  private Murderer murderer;
  private Map<Integer,Player> playerMap;

  private boolean gameRunning = false;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Game()
  {

  }

  //------------------------
  // INTERFACE
  //------------------------

  public int getCurrentPlayerTurn()
  {
    return currentPlayerTurn;
  }

  public Board getBoard()
  {
    return board;
  }

  public List<Card> getAllNonMurderCards()
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

  /**
   * Handles setting up the game
   */
   public void Setup(){
     System.out.println("Setting Up Game");
    board = new Board();

    //Create cards
    var weaponCards = CreateWeaponCards();
    var characterCards = CreateCharacterCards();
    var estateCards = CreateEstateCards();

    //Get the murder cards
    Card murderWeapon = weaponCards.remove(getRandomNumber(0,weaponCards.size()));
    Card murderEstate = estateCards.remove(getRandomNumber(0,estateCards.size()));
    Card murderCharacter = characterCards.remove(getRandomNumber(0,characterCards.size()));

    //Picks out a random murder
    murderer = new Murderer(murderWeapon,murderEstate,murderCharacter);

    //Merge and shuffle all cards
    allCards = new ArrayList<>();
    allCards.addAll(weaponCards);
    allCards.addAll(characterCards);
    allCards.addAll(estateCards);
    Collections.shuffle(allCards);

    //Get player count
     System.out.println("How Many Players, (3 or 4)?");

     try {
       InputStreamReader isr = new InputStreamReader(System.in);
       BufferedReader br = new BufferedReader(isr);

       String in = br.readLine();

       while(!(in.equals("3") | in.equals("4"))){
         System.out.println("(3 or 4)?");
         in = br.readLine();
       }
       playerNum = Integer.parseInt(in);

     }catch (IOException ioe){
       System.out.println("IO Exception raised With setting player count");
     }

    //Create players and order them for turns
    playerMap = CreatePlayers(allCards, playerNum);

    //re add the murder cards to the deck
     allCards.add(murderWeapon);
     allCards.add(murderCharacter);
     allCards.add(murderEstate);

  }

  /**
   * Handles running the game
   */
  public void Run(){
    System.out.println("Starting Game");

    gameRunning = true;
    while(gameRunning){
        passOverDevice();

        //Display board at start of turn
        DisplayBoard();

        DisplayTurnInfo();
      //Get a valid input
      boolean validInput = false;

      try{
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);

        //need to roll dice for max movement, display
          int[] dice = rollDice();
          System.out.println("You can move " + dice[2] + " cells");
        System.out.println("Where do you want to move?");
        String input =  input = br.readLine();

        //Handle quit
        if(input.equals("q") | input.equals("quit")){
          System.out.println("Stopping Game");
          return;
        }

        //validate input
        while(!CheckValidInput(input)) {
            if(input.equals("q") | input.equals("quit")){
                System.out.println("Stopping Game");
                return;
            }

          System.out.println("Input not valid");
          input = br.readLine();
        }
        //do move
          // TODO: implement player movement


      }
      catch (IOException ioe){
        System.out.println("IO Exception raised With Move Input");
      }

      NextPlayerTurn();
    }
  }

    /**
     * Rolls dice and returns the result as array
     * [0] = dice 1
     * [1] = dice 2
     * [2] = total
     * @return
     */
    private int[] rollDice() {
        int[] dice = new int[3];
        dice[0] = getRandomNumber(1,6);
        dice[1] = getRandomNumber(1,6);
        dice[2] = dice[0] + dice[1];
        System.out.println("You rolled a " + dice[0] + " and a " + dice[1]);
        return dice;
    }

    /**
     * Gives player opportunity to pass over device
     * (So no one sees other players cards)
     */
    private void passOverDevice() {
        System.out.println("Pass over device to " + playerMap.get(currentPlayerTurn).getName());
        System.out.println("Press enter to continue");
        try {
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(isr);
            String input = br.readLine();
        } catch (IOException ioe) {
            System.out.println("IO Exception raised With Move Input");
        }
    }

    private int getRandomNumber(int min,int max){
    return (int) (Math.random() * (max - min) + min);
  }

  /**
   * Creates and returns a list of Weapon cards
   */
  private List<Card> CreateWeaponCards(){
     List<Card> cards = new ArrayList<>();
     cards.add(new Card(Card.CardType.Weapon, "Broom"));
     cards.add(new Card(Card.CardType.Weapon, "Scissors"));
     cards.add(new Card(Card.CardType.Weapon, "Knife"));
     cards.add(new Card(Card.CardType.Weapon, "Shovel"));
     cards.add(new Card(Card.CardType.Weapon, "IPad"));
     return cards;
   }
  /**
   * Creates and returns a list of Character cards
   */
  private List<Card> CreateCharacterCards(){
    List<Card> cards = new ArrayList<>();
    cards.add(new Card(Card.CardType.Character, "Lucillia"));
    cards.add(new Card(Card.CardType.Character, "Bert"));
    cards.add(new Card(Card.CardType.Character, "Malina"));
    cards.add(new Card(Card.CardType.Character, "Percy"));
    return cards;
  }
  /**
   * Creates and returns a list of Estate cards
   */
  private List<Card> CreateEstateCards(){
    List<Card> cards = new ArrayList<>();
    cards.add(new Card(Card.CardType.Estate, "Haunted House"));
    cards.add(new Card(Card.CardType.Estate, "Manic Manor"));
    cards.add(new Card(Card.CardType.Estate, "Visitation Villa"));
    cards.add(new Card(Card.CardType.Estate, "Calamity Castle"));
    cards.add(new Card(Card.CardType.Estate, "Peril Palace"));
    return cards;
  }

  /**
   * Creates players and allocates cards to them
   */
  Map<Integer, Player> CreatePlayers(List<Card> cards, int playerNum){
    //Only handling 3 or 4 players case

    var players = new ArrayList<Player>();
    players.add(new Player("Lucilla"));
    players.add(new Player("Bert"));
    players.add(new Player("Malina"));

    //only adds player if needed
    if(playerNum>= 4) players.add(new Player("Percy"));

    //Goes through every card
    int i = 0;
    while(i < cards.size()){
      //Adds a card to each player until no cards are left
      for (Player p: players) {
        p.addCard(cards.get(i));
        i++;
        if(i >= cards.size()) break;
      }
    }

    //Turn order
    var playerMap = new HashMap<Integer, Player>();
    for(int p = 0; p < players.size(); p++){
      playerMap.put(p, players.get(p));
    }
    return playerMap;
  }


  /**
   * Gets and displays the board to the screen
   */
   private void DisplayBoard(){
       this.board.displayBoard();
   }

  /**
   * Displays who's turn it is
   */
  private void DisplayTurnInfo(){
    System.out.println("Player " + (currentPlayerTurn + 1) +"'s turn: " + playerMap.get(currentPlayerTurn).toString());
  }

  /**
   * Sets to next player turn
   */
  private void NextPlayerTurn(){
     System.out.println("End turn");
    currentPlayerTurn++;
    if(currentPlayerTurn>=playerNum) currentPlayerTurn = 0;
  }

  /**
   * Checks input is valid
   */
  private boolean CheckValidInput(String input){
     if(input.equals("T")) return true;
    return false;
  }

  /**
   * returns the card class of a card by its name, returns null if cant be found
   */
   public Card getCardByName(String cardName){

      for(Card c : allCards){
        if(c.getCardName().equals(cardName)) return c;
      }
      return null;
   }

}
