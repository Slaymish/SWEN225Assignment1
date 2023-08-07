import java.io.BufferedReader;
import java.io.IOException;
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

        //remove the 4th player from the deck if only 3 players
        if(playerNum == 3) {
            allCards.remove(getCardByName("Percy"));
        }
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
                        System.out.println(ConsoleCommands.RED + "Invalid Move, try again" + ConsoleCommands.RESET);
                        displayMoveHelp();
                    }

                    if (!board.isMoveValid(currentPlayerTurn, move[0], move[1])) {
                        System.out.println(ConsoleCommands.RED + "You can't move there! try again" + ConsoleCommands.RESET);
                        validInput = false;
                    }
                }

                // move player
                // TODO : let player move into doors
                System.out.println("Moving player " + currentPlayerTurn + " by (x=" + move[0] + ",y=" + move[1] + ")");
                playerMap.get(currentPlayerTurn).setPositionWithOffset(move[0], move[1]);
                board.updatePeopleOnBoard(); 

                if (currentPlayerInEstate()) {
                    Estate estate = getBoard().getEstateList().stream()
                            .filter(e -> e.playerIsInside(getCurrentPlayer().getPosition()[0], getCurrentPlayer().getPosition()[1]))
                            .findAny().get();
                    System.out.println("You are in " + ConsoleCommands.inBlue(estate.name));
                    handleAttempt(estate);
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
        // for each estate
        // playerIsInside()
        Optional<Estate> estateOptional = getBoard().getEstateList().stream()
                .filter(estate -> estate.playerIsInside(x, y))
                .findAny();

        return estateOptional.isPresent();
    }

    /**
     * Handles player guessing/solving
     * Only called if player is in an estate
     *
     * @throws IOException
     */
    private void handleAttempt(Estate estate) throws IOException {
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
            guess(estate);
        } else if(guessOrSolve.equals("SOLVE")) {
            solve();
        }
    }
    
    /**
     * handles guess attempts
     */
    
    private void guess(Estate estate) throws IOException {
        Player currentPlayer = getCurrentPlayer();
        List<String> weapons = getWeapons();
        List<String> characters = getCharacters();
        
        System.out.println("Weapons: " + ConsoleCommands.RED + weapons + ConsoleCommands.RESET);
        System.out.println("Characters: " + ConsoleCommands.CYAN + characters + ConsoleCommands.RESET);
        System.out.println("Select a " + ConsoleCommands.inRed("weapon") + " for your guess");
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean validInput = false; 
        String input;
        String weapon = "";
        while(!validInput) {
            input = br.readLine();
            input = input.toUpperCase(); // Makes parsing easier
            
            for(String s : weapons) {
                if (input.equals(s)) {
                    validInput = true;
                    weapon = input;
                    break;
                }
            }
            
            if(!validInput) {
                System.out.println("Enter valid weapon");
            }
        }
        
        System.out.println("Select a " + ConsoleCommands.inCyan("character") + " for your guess");
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
                if (input.equals(s)) {
                    validInput = true;
                    character = input;
                    break;
                }
            }
            
            if(!validInput) {
                System.out.println("Enter valid character");
            }
        }

        // print guess
        System.out.println("Your guess is: " + character + " killed in the " + estate.name + " with the " + weapon);

        int startTurnNumber = currentPlayerTurn;
        int rotationNumber = currentPlayerTurn;

        // goes around all the players to refute the guess
        rotationNumber++;
        if(rotationNumber >= playerNum) rotationNumber = 0;
        while(rotationNumber != startTurnNumber) {
            nextGuessRotation(rotationNumber);

            System.out.println(playerMap.get(currentPlayerTurn).getName() + " guessed that " + character + " killed in the " + estate.name + " with the " + weapon);
            System.out.println("Your cards: " + playerMap.get(rotationNumber).getCards());

            boolean refuted = false;
            // check if any cards player has are contained in the guess
            for(Card c : playerMap.get(rotationNumber).getCards()) {
                if(Objects.equals(c.getCardName().toUpperCase(), character)) {
                    if(refuteGuess(c)) {
                        refuted = true;
                        break;
                    }
                }
                if(Objects.equals(c.getCardName().toUpperCase(), weapon)) {
                    if(refuteGuess(c)) {
                        refuted = true;
                        break;
                    }
                }
                if(Objects.equals(c.getCardName().toUpperCase(), estate.name)) {
                    if(refuteGuess(c)) {
                        refuted = true;
                        break;
                    }
                }
            }

            if(refuted) {
                break;
            }
            rotationNumber++;
            if(rotationNumber >= playerNum) rotationNumber = 0;
        }
    }

    private boolean refuteGuess(Card c) throws IOException {
        System.out.println("You can refute the guess with your " + c.getCardName() + " card");
        System.out.println("Would you like to refute? (YES/NO)");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean validInput = false;
        String input;
        String answer = "";
        while(!validInput) {
            input = br.readLine();
            input = input.toUpperCase(); // Makes parsing easier

            if(input.equals("YES") | input.equals("NO")) {
                validInput = true;
                answer = input;
            }

            if(!validInput) {
                System.out.println("Enter valid answer (YES/NO)");
            }
        }

        if(answer.equals("YES")) {
            System.out.println("Guess refuted using " + c.getCardName().toUpperCase() + " card");
            System.out.println("Press enter to continue");
            try {
                InputStreamReader isr = new InputStreamReader(System.in);
                BufferedReader bufferedreader = new BufferedReader(isr);
                String enter = bufferedreader.readLine();
            } catch (IOException ioe) {
                System.out.println("IO Exception raised With Guess Input");
            }

            return true;
        }
        return false;
    }

    private void nextGuessRotation(int rotationNumber) {
        System.out.println("Pass over device to " + playerMap.get(rotationNumber).getName());
        System.out.println("Press enter to continue");
        try {
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader bufferedreader = new BufferedReader(isr);
            String enter = bufferedreader.readLine();
        } catch (IOException ioe) {
            System.out.println("IO Exception raised With Guess Input");
        }
        ConsoleCommands.clearScreen();
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
        // TODO : add some tests for this
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
        ConsoleCommands.clearScreen();
        System.out.println("Pass over device to " + playerMap.get(currentPlayerTurn).getName());
        System.out.println("Press enter to continue");
        try {
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(isr);
            String input = br.readLine();
        } catch (IOException ioe) {
            System.out.println("IO Exception raised With Move Input");
        }
        ConsoleCommands.clearScreen();
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
        System.out.println("Player " + (currentPlayerTurn + 1) + "'s turn: " +
               getCurrentPlayer().toString());
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

class ConsoleCommands {
    public static final String BLACK = "\033[0;30m";   // BLACK
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String CYAN = "\033[0;36m";    // CYAN
    public static final String WHITE = "\033[0;37m";   // WHITE
    public static final String RESET = "\033[0m";  // Text Reset

    // Bold
    public static final String BLACK_BOLD = "\033[1;30m";  // BLACK
    public static final String RED_BOLD = "\033[1;31m";    // RED
    public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
    public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
    public static final String BLUE_BOLD = "\033[1;34m";   // BLUE
    public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
    public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
    public static final String WHITE_BOLD = "\033[1;37m";  // WHITE

    public static String inYellow(String text){
        return YELLOW_BOLD + text + RESET;
    }

    public static String inRed(String text){
        return RED + text + RESET;
    }

    public static String inGreen(String text){
        return GREEN + text + RESET;
    }

    public static String inBlue(String text){
        return BLUE + text + RESET;
    }

    public static String inPurple(String text){
        return PURPLE + text + RESET;
    }

    public static String inCyan(String text){
        return CYAN + text + RESET;
    }

    public static void clearScreen() {
        System.out.println(System.lineSeparator().repeat(50));
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
