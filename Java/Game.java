import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Game {

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //Game Attributes
    private int playerNum = 4;
    private int currentPlayerTurn = 0;
    private Board board;
    private List<Card> allCards;
    private Murderer murderer;
    private Map<Integer, Player> playerMap;

    private boolean gameRunning = false;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Game() {

    }

    //------------------------
    // INTERFACE
    //------------------------

    public int getCurrentPlayerTurn() {
        return currentPlayerTurn;
    }

    public Board getBoard() {
        return board;
    }

    public List<Card> getAllNonMurderCards() {
        return allCards;
    }

    public Murderer getMurderer() {
        return murderer;
    }

    public Map<Integer, Player> getPlayerMap() {
        return playerMap;
    }

    /**
     * Handles setting up the game
     */
    public void Setup() {
        System.out.println("Setting Up Game");
        board = new Board();

        //Create cards
        var weaponCards = CreateWeaponCards();
        var characterCards = CreateCharacterCards();
        var estateCards = CreateEstateCards();

        //Get the murder cards
        Card murderWeapon = weaponCards.remove(getRandomNumber(0, weaponCards.size()));
        Card murderEstate = estateCards.remove(getRandomNumber(0, estateCards.size()));
        Card murderCharacter = characterCards.remove(getRandomNumber(0, characterCards.size()));

        //Picks out a random murder
        murderer = new Murderer(murderWeapon, murderEstate, murderCharacter);

        //Merge and shuffle all cards
        allCards = new ArrayList<>();
        allCards.addAll(weaponCards);
        allCards.addAll(characterCards);
        allCards.addAll(estateCards);
        Collections.shuffle(allCards);
    }

    public void setupPlayers() {
        //Get player count
        System.out.println("How Many Players, (3 or 4)?");

        try {
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(isr);

            String in = br.readLine();

            while (!(in.equals("3") | in.equals("4"))) {
                System.out.println("(3 or 4)?");
                in = br.readLine();
            }
            playerNum = Integer.parseInt(in);

        } catch (IOException ioe) {
            System.out.println("IO Exception raised With setting player count");
        }

        //Create players and order them for turns
        playerMap = CreatePlayers(allCards, playerNum);

        //re add the murder cards to the deck
        allCards.addAll(murderer.getCards());
    }

    /**
     * Handles running the game
     */
    public void Run() {
        System.out.println("Starting Game");

        gameRunning = true;
        while (gameRunning) {
            passOverDevice();

            //Display board at start of turn
            DisplayBoard();

            DisplayTurnInfo();
            //Get a valid input
            boolean validInput = false;

            try {
                InputStreamReader isr = new InputStreamReader(System.in);
                BufferedReader br = new BufferedReader(isr);
                
                int[] dice = rollDice();
                System.out.println("You can move " + dice[2] + " cells");
                System.out.println("Where do you want to move?");

                String input;
                int[] move = new int[2];

                // Get move input
                while (!validInput) {
                    input = br.readLine();
                    input = input.toUpperCase(); // Makes parsing easier

                    if (input.equals("Q") | input.equals("QUIT")) {
                        System.out.println("Stopping Game");
                        return;
                    }

                    try {
                        move = parseInput(input, dice[2]);
                        validInput = true;
                    } catch (IllegalArgumentException iae) { // If error in parsing
                        System.out.println("Invalid Move, try again");
                        displayMoveHelp();
                    }

                    if (!board.isMoveValid(currentPlayerTurn, move[0], move[1])) {
                        System.out.println("You can't move there! try again");
                        validInput = false;
                    }
                }

                // move player
                // TODO : let player move into doors
                System.out.println("Moving player " + currentPlayerTurn + " by (x=" + move[0] + ",y=" + move[1] + ")");
                playerMap.get(currentPlayerTurn).setPositionWithOffset(move[0], move[1]);
                board.updatePeopleOnBoard(); 

                if (currentPlayerInEstate()) {
                    System.out.println("You are in an estate");
                    handleAttempt();
                }
            } catch (IOException ioe) {
                System.out.println("IO Exception raised With Move Input");
            }

            NextPlayerTurn();
        }
    }

    /**
     * Returns true if the current player is in an estate
     *
     * @return
     */
    private boolean currentPlayerInEstate() {
        Player currentPlayer = playerMap.get(currentPlayerTurn);
        int[] currentPlayerPosition = currentPlayer.getPosition();
        int x = currentPlayerPosition[0];
        int y = currentPlayerPosition[1];
        
        // TODO fix these some are bugged
        
        //haunted house 2, 2, 6, 6
        if(x>=2 && y>=2 && x<=6 && y<=6) {
            return true;
        }
        
        //manic manor 17, 2, 21, 2
        if(x>=17 && y>=2 && x<=21 && y<=2) {
            return true;
        }
        
        //calamity castle 2, 17, 2, 21
        if(x>=2 && y>=17 && x<=2 && y<=21) {
            return true;
        }
        
        //peril palace 17, 17, 21, 21
        if(x>=17 && y>=17 && x<=21 && y<=21) {
            return true;
        }
        
        //visitation villa 9, 10, 14, 13
        if(x>=0 && y>=10 && x<=14 && y<=13) {
            return true;
        }
        
        return false;
    }

    /**
     * Handles player guessing/solving
     * Only called if player is in an estate
     *
     * @throws IOException
     */
    private void handleAttempt() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Do you want to guess or solve?");
        boolean validInput = false;
        String input;
        String guessOrSolve = "";
        while (!validInput) {
            input = br.readLine();
            input = input.toUpperCase(); // Makes parsing easier

            if (input.equals("Q") | input.equals("QUIT")) {
                System.out.println("Stopping Game");
                return;
            }

            if (input.equals("GUESS") | input.equals("SOLVE")) {
                validInput = true;
                guessOrSolve = input;
            } else {
                System.out.println("Invalid input, try again");
            }
        }
        
        if(guessOrSolve.equals("GUESS")) {
            guess();
        } else if(guessOrSolve.equals("SOLVE")) {
            solve();
        }
    }
    
    /**
     * handles guess attempts
     */
    
    private void guess() throws IOException {
        Player currentPlayer = getCurrentPlayer();
        List<String> weapons = getWeapons();
        List<String> characters = getCharacters();
        
        System.out.println("Weapons: " + weapons);
        System.out.println("Characters: " + characters);
        System.out.println("Select a weapon for your guess");
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean validInput = false; 
        String input;
        String weapon = "";
        while(!validInput) {
            input = br.readLine();
            input = input.toUpperCase(); // Makes parsing easier
            
            if (input.equals("Q") | input.equals("QUIT")) {
                System.out.println("Stopping Game");
                return;
            }
            
            for(String s : weapons) {
                if(input.equals(s)) {
                    validInput = true;
                    weapon = input;
                }
            }
            
            if(!validInput) {
                System.out.println("Enter valid weapon");
            }
        }
        
        System.out.println("Select a character for your guess");
        validInput = false;
        String character = "";
        while(!validInput) {
            input = br.readLine();
            input = input.toUpperCase(); // Makes parsing easier
            
            if (input.equals("Q") | input.equals("QUIT")) {
                System.out.println("Stopping Game");
                return;
            }
            
            for(String s : characters) {
                if(input.equals(s)) {
                    validInput = true;
                    character = input;
                }
            }
            
            if(!validInput) {
                System.out.println("Enter valid character");
            }
        }
        
        System.out.println("Your guess is: " + character + " killed using " + weapon + " in this room");
        
        //TODO implement refutations
    }
    
    /**
     * handles solve attempts
     */
    private void solve() {
        //TODO implement solve attempt
    }
    
    /**
     * gets list of weapon cards
     */
    private List<String> getWeapons() {
        List<String> weapons = new ArrayList<String>();
        for(Card c : allCards) {
            if(c.getCardType() == Card.CardType.Weapon) {
                weapons.add(c.getCardName().toUpperCase());
            }
        }
        return weapons;
    }
    
    /**
     * gets list of character cards
     */
    private List<String> getCharacters() {
        List<String> characters = new ArrayList<String>();
        for(Card c : allCards) {
            if(c.getCardType() == Card.CardType.Character) {
                characters.add(c.getCardName().toUpperCase());
            }
        }
        return characters;
    }

    /**
     * Parses input into a movement array
     * eg input = "UP 3 RIGHT 2"
     * case insensitive
     * returns [x,y] offset
     *
     * @param input
     * @return
     */
    private int[] parseInput(String input, int maxMove) {
        // TODO : RIGHT AND DOWN ARE SWITCHED ??? somethings up at least
        int[] move = new int[2];
        Scanner scanner = new Scanner(input);
        System.out.println(input);
        try {
            while (scanner.hasNext()) {
                String direction = scanner.next();
                switch (direction) {
                    case "UP":
                        move[1] -= scanner.nextInt();
                        break;
                    case "DOWN":
                        move[1] += scanner.nextInt();
                        break;
                    case "LEFT":
                        move[0] -= scanner.nextInt();
                        break;
                    case "RIGHT":
                        move[0] += scanner.nextInt();
                        break;
                    default:
                        System.out.println("Invalid Direction");
                        throw new IllegalArgumentException("Invalid Direction");
                }
            }
        } catch (InputMismatchException ime) {
            throw new IllegalArgumentException("Mismatched input");
        }

        if (move[0] + move[1] > maxMove) {
            throw new IllegalArgumentException("Move too large");
        }

        return move;
    }

    private void displayMoveHelp() {
        System.out.println("Enter a move in the form of:");
        System.out.println("UP/DOWN/LEFT/RIGHT <number>");
        System.out.println("eg: UP 3 RIGHT 2");
    }

    /**
     * Rolls dice and returns the result as array
     * [0] = dice 1
     * [1] = dice 2
     * [2] = total
     *
     * @return
     */
    private int[] rollDice() {
        int[] dice = new int[3];
        dice[0] = getRandomNumber(1, 6);
        dice[1] = getRandomNumber(1, 6);
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

    private int getRandomNumber(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    /**
     * Creates and returns a list of Weapon cards
     */
    private List<Card> CreateWeaponCards() {
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
    private List<Card> CreateCharacterCards() {
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
    private List<Card> CreateEstateCards() {
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
    Map<Integer, Player> CreatePlayers(List<Card> cards, int playerNum) {
        //Only handling 3 or 4 players case

        var players = new ArrayList<Player>();
        players.add(new Player("Lucilla"));
        players.add(new Player("Bert"));
        players.add(new Player("Malina"));

        //only adds player if needed
        if (playerNum >= 4) players.add(new Player("Percy"));

        //Goes through every card
        int i = 0;
        while (i < cards.size()) {
            //Adds a card to each player until no cards are left
            for (Player p : players) {
                p.addCard(cards.get(i));
                i++;
                if (i >= cards.size()) break;
            }
        }

        // Set player start position
        players.get(0).setPosition(11, 1); // Lucilla
        players.get(1).setPosition(1, 9); // Bert
        players.get(2).setPosition(9, 22); // Malina
        if (playerNum >= 4) players.get(3).setPosition(22,14); // Percy

        //Turn order
        var playerMap = new HashMap<Integer, Player>();
        for (int p = 0; p < players.size(); p++) {
            playerMap.put(p, players.get(p));
        }

        board.BuildPeople(playerMap); // Build people on board

        return playerMap;
    }


    /**
     * Gets and displays the board to the screen
     */
    private void DisplayBoard() {
        this.board.displayBoard();
    }

    /**
     * Displays who's turn it is
     */
    private void DisplayTurnInfo() {
        System.out.println("Player " + (currentPlayerTurn + 1) + "'s turn: " + playerMap.get(currentPlayerTurn).toString());
    }

    /**
     * Sets to next player turn
     */
    private void NextPlayerTurn() {
        System.out.println("End turn");
        currentPlayerTurn++;
        if (currentPlayerTurn >= playerNum) currentPlayerTurn = 0;
    }

    /**
     * Checks input is valid
     */
    private boolean CheckValidInput(String input) {
        return input.equals("T");
    }

    /**
     * returns the card class of a card by its name, returns null if cant be found
     */
    public Card getCardByName(String cardName) {

        for (Card c : allCards) {
            if (c.getCardName().equals(cardName)) return c;
        }
        return null;
    }
    
    /**
     * gets current player
     */
    public Player getCurrentPlayer() {
        return playerMap.get(currentPlayerTurn);
    }
}
