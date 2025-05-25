package uno;

import org.junit.jupiter.api.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestUno {

    private String aaron = "Aaron";
    private String felpa = "Felpa";
    private String dan = "Dan";
    private String red = "Red";
    private String blue = "Blue";
    private String green = "Green";
    private String yellow = "Yellow";
    private ArrayList<Card> createDeck() {
        ArrayList<Card> deck = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            deck.add(new NumberCard(red, i % 10));
            deck.add(new NumberCard(blue, i % 10));
            deck.add(new NumberCard(green, i % 10));
            deck.add(new NumberCard(yellow, i % 10));
        }
        deck.add(new SkipCard(red));
        deck.add(new SkipCard(blue));
        deck.add(new DrawTwoCard(red));
        deck.add(new DrawTwoCard(blue));
        deck.add(new ReverseCard(red));
        deck.add(new ReverseCard(blue));
        deck.add(new Wildcard());
        deck.add(new Wildcard());
        return deck;
    }

    private Player createPlayerWithCards(String name, Card... cards) {
        ArrayList<Card> cardList = new ArrayList<>(Arrays.asList(cards));
        return new Player(name, cardList);
    }
    @Test
    void testGameCreatedCorrectly() {
        ArrayList<Card> deck = createDeck();
        Player player1 = new Player(aaron);
        Player player2 = new Player(felpa);

        Game game = new Game(deck, 7, player1, player2);

        assertNotNull(game);
        assertEquals(2, game.getPlayers().size());
        assertEquals(aaron, game.getCurrentPlayer().getName());
        assertNotNull(game.getLastCardPlayed());
    }

    @Test
    void testTurnManagement() {
        ArrayList<Card> deck = createDeck();
        NumberCard redFive = new NumberCard(red, 5);
        NumberCard redSix = new NumberCard(red, 6);

        Player player1 = createPlayerWithCards(aaron, redFive);
        Player player2 = createPlayerWithCards(felpa, redSix);

        Game game = new Game(deck, 7, player1, player2);
        game.setLastCardPlayed(new NumberCard(red, 4));
        assertEquals(aaron, game.getCurrentPlayer().getName());

        game.playerPlayCard(player1, redFive);
        assertEquals(felpa, game.getCurrentPlayer().getName());
    }

    @Test
    void testCardCompatibility() {
        NumberCard redFive = new NumberCard(red, 5);
        NumberCard redSeven = new NumberCard(red, 7);
        NumberCard blueFive = new NumberCard(blue, 5);
        NumberCard blueEight = new NumberCard(blue, 8);
        SkipCard redSkip = new SkipCard(red);
        Wildcard wildcard = new Wildcard().setColor(green);

        assertTrue(redSeven.canBeStackedOnTopOf(redFive));
        assertTrue(blueFive.canBeStackedOnTopOf(redFive));
        assertFalse(blueEight.canBeStackedOnTopOf(redFive));
        assertTrue(redSkip.canBeStackedOnTopOf(redFive));
        assertTrue(wildcard.canBeStackedOnTopOf(redFive));
    }

    @Test
    void testInvalidPlays() {
        ArrayList<Card> deck = createDeck();
        NumberCard redFive = new NumberCard(red, 5);
        NumberCard blueEight = new NumberCard(blue, 8);

        Player player1 = createPlayerWithCards(aaron, redFive);
        Player player2 = createPlayerWithCards(felpa, blueEight);

        Game game = new Game(deck, 7, player1, player2);
        game.setLastCardPlayed(new NumberCard(green, 3));

        int initialCards = player1.getCards().size();

        game.playerPlayCard(player1, redFive);

        assertEquals(initialCards + 1, player1.getCards().size());
        assertTrue(player1.getCards().contains(redFive));
    }

    @Test
    void testPlayingOutOfTurn() {
        ArrayList<Card> deck = createDeck();
        NumberCard redFive = new NumberCard(red, 5);
        NumberCard redSix = new NumberCard(red, 6);

        Player player1 = createPlayerWithCards(aaron, redFive);
        Player player2 = createPlayerWithCards(felpa, redSix);

        Game game = new Game(deck, 7, player1, player2);
        game.setLastCardPlayed(new NumberCard(red, 4));

        game.playerPlayCard(player1, redFive);

        int player1InitialCards = player1.getCards().size();

        game.playerPlayCard(player1, new NumberCard(red, 7));

        assertEquals(player1InitialCards + 1, player1.getCards().size());
    }

    @Test
    void testUnoPenalty() {
        ArrayList<Card> deck = createDeck();
        NumberCard redFive = new NumberCard(red, 5);

        Player player1 = createPlayerWithCards(aaron, redFive);
        Player player2 = createPlayerWithCards(felpa, new NumberCard(blue, 6));

        Game game = new Game(deck, 7, player1, player2);
        game.setLastCardPlayed(new NumberCard(red, 4));

        game.playerPlayCard(player1, redFive);

        Player player3 = createPlayerWithCards("Player3", redFive, new NumberCard(red, 6));
        Game game2 = new Game(createDeck(), 7, player3, player2);
        game2.setLastCardPlayed(new NumberCard(red, 4));

        int initialCards = player3.getCards().size();
        game2.playerPlayCard(player3, redFive);

        assertEquals(initialCards + 1, player3.getCards().size());
    }

    @Test
    void testUnoShoutPreventsPenalty() {
        ArrayList<Card> deck = createDeck();
        NumberCard redFive = new NumberCard(red, 5);
        NumberCard redSix = new NumberCard(red, 6);

        Player player1 = createPlayerWithCards(aaron, redFive, redSix);
        Player player2 = createPlayerWithCards(felpa, new NumberCard(blue, 6));

        Game game = new Game(deck, 7, player1, player2);
        game.setLastCardPlayed(new NumberCard(red, 4));

        player1.shoutUno();

        int initialCards = player1.getCards().size();
        game.playerPlayCard(player1, redFive);

        assertEquals(initialCards - 1, player1.getCards().size());
    }

    @Test
    void testVictoryCondition() {
        ArrayList<Card> deck = createDeck();
        NumberCard redFive = new NumberCard(red, 5);

        Player player1 = createPlayerWithCards(aaron, redFive);
        Player player2 = createPlayerWithCards(felpa, new NumberCard(blue, 6));

        Game game = new Game(deck, 7, player1, player2);
        game.setLastCardPlayed(new NumberCard(red, 4));
        game.playerPlayCard(player1, redFive);
        assertTrue(player1.checkForVictory());
    }

    @Test
    void testSkipCard() {
        ArrayList<Card> deck = createDeck();
        SkipCard redSkip = new SkipCard(red);

        Player player1 = createPlayerWithCards(aaron, redSkip);
        Player player2 = createPlayerWithCards(felpa, new NumberCard(blue, 6));
        Player player3 = createPlayerWithCards(dan, new NumberCard(green, 7));

        Game game = new Game(deck, 7, player1, player2, player3);
        game.setLastCardPlayed(new NumberCard(red, 4));

        game.playerPlayCard(player1, redSkip);

        assertEquals(dan, game.getCurrentPlayer().getName());
    }

    @Test
    void testDrawTwoCard() {
        ArrayList<Card> deck = createDeck();
        DrawTwoCard redDrawTwo = new DrawTwoCard(red);

        Player player1 = createPlayerWithCards(aaron, redDrawTwo);
        Player player2 = createPlayerWithCards(felpa, new NumberCard(blue, 6));
        Player player3 = createPlayerWithCards(dan, new NumberCard(green, 7));

        Game game = new Game(deck, 7, player1, player2, player3);
        game.setLastCardPlayed(new NumberCard(red, 4));

        int player2InitialCards = player2.getCards().size();

        game.playerPlayCard(player1, redDrawTwo);

        assertEquals(player2InitialCards + 2, player2.getCards().size());
        assertEquals(dan, game.getCurrentPlayer().getName());
    }

    @Test
    void testWildcardColorRequired() {
        ArrayList<Card> deck = createDeck();
        Wildcard wildcard = new Wildcard();

        Player player1 = createPlayerWithCards(aaron, wildcard);
        Player player2 = createPlayerWithCards(felpa, new NumberCard(blue, 6));

        Game game = new Game(deck, 7, player1, player2);
        game.setLastCardPlayed(new NumberCard(red, 4));

        assertThrows(RuntimeException.class, () -> {
            game.playerPlayCard(player1, wildcard);
        });
    }

    @Test
    void testWildcardCompatibility() {
        ArrayList<Card> deck = createDeck();
        Wildcard wildcard = new Wildcard();
        NumberCard greenFive = new NumberCard(green, 5);
        NumberCard redSix = new NumberCard(red, 6);

        Player player1 = createPlayerWithCards(aaron, wildcard);
        player1.setWildcardColor(wildcard, green);
        Player player2 = createPlayerWithCards(felpa, greenFive, redSix);

        Game game = new Game(deck, 7, player1, player2);
        game.setLastCardPlayed(new NumberCard(red, 4));
        game.playerPlayCard(player1, wildcard);
        game.playerPlayCard(player2, greenFive);

        assertEquals(aaron, game.getCurrentPlayer().getName());
    }

    @Test
    void testWildcardColorSetting() {
        Wildcard wildcard = new Wildcard();
        Player player1 = createPlayerWithCards(aaron, new Wildcard());
        Player player2 = createPlayerWithCards(felpa, new ReverseCard(blue));

        assertEquals("Any", wildcard.getColor());

        player1.setWildcardColor(wildcard, blue);
        assertEquals(blue, wildcard.getColor());

        Wildcard otherWildcard = new Wildcard();
        assertThrows(RuntimeException.class, () -> {
            player2.setWildcardColor(otherWildcard, red);
        });
    }

    @Test
    void testReverseCard() {
        ArrayList<Card> deck = createDeck();
        ReverseCard redReverse = new ReverseCard(red);

        Player player1 = createPlayerWithCards(aaron, redReverse);
        Player player2 = createPlayerWithCards(felpa, new NumberCard(blue, 6));
        Player player3 = createPlayerWithCards(dan, new NumberCard(green, 7));

        Game game = new Game(deck, 7, player1, player2, player3);
        game.setLastCardPlayed(new NumberCard(red, 4));

        assertEquals(aaron, game.getPlayers().get(0).getName());
        assertEquals(felpa, game.getPlayers().get(1).getName());
        assertEquals(dan, game.getPlayers().get(2).getName());

        game.playerPlayCard(player1, redReverse);

        assertEquals(dan, game.getPlayers().get(0).getName());
        assertEquals(felpa, game.getPlayers().get(1).getName());
        assertEquals(aaron, game.getPlayers().get(2).getName());
    }

    @Test
    void testUnoShoutWithOneCard() {
        ArrayList<Card> deck = createDeck();
        NumberCard redFive = new NumberCard(red, 5);

        Player player1 = createPlayerWithCards(aaron, redFive);

        player1.shoutUno();
        assertTrue(player1.shoutedUno());

        player1.resetUnoShout();
        assertFalse(player1.shoutedUno());
    }

    @Test
    void testPlayerDoesntHaveCard() {
        ArrayList<Card> deck = createDeck();
        NumberCard redFive = new NumberCard(red, 5);
        NumberCard blueSix = new NumberCard(blue, 6);

        Player player1 = createPlayerWithCards(aaron, redFive);
        Player player2 = createPlayerWithCards(felpa, new NumberCard(blue, 7));

        Game game = new Game(deck, 7, player1, player2);
        game.setLastCardPlayed(new NumberCard(red, 4));

        assertThrows(RuntimeException.class, () -> {
            game.playerPlayCard(player1, blueSix);
        });
    }

    @Test
    void testEmptyDeck() {
        ArrayList<Card> emptyDeck = new ArrayList<>();
        Player player1 = new Player(aaron);

        assertThrows(NoSuchElementException.class, () -> {
            new Game(emptyDeck, 7, player1);
        });
    }

    @Test
    void testSpecialCardCompatibility() {
        SkipCard redSkip = new SkipCard(red);
        SkipCard blueSkip = new SkipCard(blue);
        DrawTwoCard redDrawTwo = new DrawTwoCard(red);
        ReverseCard redReverse = new ReverseCard(red);

        assertTrue(blueSkip.canBeStackedOnTopOf(redSkip));

        assertTrue(redDrawTwo.canBeStackedOnTopOf(redSkip));
        assertTrue(redReverse.canBeStackedOnTopOf(redSkip));
    }

    @Test
    void testMultipleWildcards() {
        Wildcard wildcard1 = new Wildcard().setColor(red);
        Wildcard wildcard2 = new Wildcard().setColor(blue);

        assertTrue(wildcard2.canBeStackedOnTopOf(wildcard1));
        assertTrue(wildcard1.canBeStackedOnTopOf(wildcard2));
    }
}