import java.io.*;

import org.junit.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class GameTests {
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private Game game;

    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    @Before
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    @BeforeEach
    public void init() { // Start a new game
        game = new Game();
        game.Setup();
        provideInput("4\n");
        game.setupPlayers();
    }
    @After
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    @Test
    public void testCase1() {
        String board = game.getBoard().displayBoard();
        String shouldBe = """
                _|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|
                _|_|_|_|_|_|_|_|_|_|_|L|_|_|_|_|_|_|_|_|_|_|_|_|
                _|_|I|I|I|I|I|_|_|_|_|_|_|_|_|_|_|I|I|I|I|I|_|_|
                _|_|I|I|I|I|D|_|_|_|_|_|_|_|_|_|_|I|I|I|I|I|_|_|
                _|_|I|I|I|I|I|_|_|_|_|_|_|_|_|_|_|I|I|I|I|I|_|_|
                _|_|I|I|I|I|I|_|_|_|_|W|W|_|_|_|_|D|I|I|I|I|_|_|
                _|_|I|I|I|D|I|_|_|_|_|W|W|_|_|_|_|I|I|I|D|I|_|_|
                _|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|
                _|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|
                _|B|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|
                _|_|_|_|_|_|_|_|_|I|I|I|D|I|I|_|_|_|_|_|_|_|_|_|
                _|_|_|_|_|W|W|_|_|I|I|I|I|I|D|_|_|W|W|_|_|_|_|_|
                _|_|_|_|_|W|W|_|_|D|I|I|I|I|I|_|_|W|W|_|_|_|_|_|
                _|_|_|_|_|_|_|_|_|I|I|D|I|I|I|_|_|_|_|_|_|_|_|_|
                _|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|P|_|
                _|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|
                _|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|
                _|_|I|D|I|I|I|_|_|_|_|W|W|_|_|_|_|I|D|I|I|I|_|_|
                _|_|I|I|I|I|D|_|_|_|_|W|W|_|_|_|_|I|I|I|I|I|_|_|
                _|_|I|I|I|I|I|_|_|_|_|_|_|_|_|_|_|I|I|I|I|I|_|_|
                _|_|I|I|I|I|I|_|_|_|_|_|_|_|_|_|_|D|I|I|I|I|_|_|
                _|_|I|I|I|I|I|_|_|_|_|_|_|_|_|_|_|I|I|I|I|I|_|_|
                _|_|_|_|_|_|_|_|_|M|_|_|_|_|_|_|_|_|_|_|_|_|_|_|
                _|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|
                                
                GLOSSARY OF SYMBOLS:
                | W = WALL           |
                | I = ESTATE WALL    |
                | P = PERSON         |
                | D = DOOR OF ESTATE |"""; // This is the board that should be displayed
        assertEquals(board, shouldBe);
    }
}