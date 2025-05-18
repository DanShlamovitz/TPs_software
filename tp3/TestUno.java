package uno;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestUno {

    @Test
    public void testPlayCardSubtractsCardAndMovesTurn() {
        Player aaron = new Player("Aaron");
        Player felpa = new Player("Felpa");
        Player proca = new Player("Proca");
        Player juansua = new Player("Juansua");

        TurnManager game = new TurnManager(aaron, felpa, proca, juansua);
        assertEquals(game.getCurrentPlayer(), aaron);
        System.out.println(aaron.getCards().getFirst().getSymbol());

        game.playerPlayCard(aaron, aaron.getCards().getFirst());
        assertEquals(6, aaron.getCards().size());
        assertEquals(felpa, game.getCurrentPlayer());

    }
    @Test
    public void testGameInitialization() {
        Player aaron = new Player("Aaron");
        Player felpa = new Player("Felpa");
        Player proca = new Player("Proca");
        Player juansua = new Player("Juansua");

        TurnManager game = new TurnManager(aaron, felpa, proca, juansua);

        // Check that all players have 7 cards initially
        for (Player player : game.getPlayers()) {
            assertEquals(7, player.getCards().size());
        }

        // The first player should be assigned as the current player
        assertEquals(aaron, game.getCurrentPlayer());

        // NoCard is initially the last card played
        assertEquals("NoCard", game.getLastCardPlayed().getSymbol());
    }

    @Test
    public void testPlayCardAndMoveTurn() {
        Player aaron = new Player("Aaron");
        Player felpa = new Player("Felpa");
        TurnManager game = new TurnManager(aaron, felpa);

        int initialAaronCards = aaron.getCards().size();

        // Play a card
        game.playerPlayCard(aaron, aaron.getCards().getFirst());

        // Check that the card was removed from Aaron's hand
        assertEquals(initialAaronCards - 1, aaron.getCards().size());

        // The turn should move to Felpa
        assertEquals(felpa, game.getCurrentPlayer());
    }

    @Test
    public void testDrawTwoEffect() {
        Player aaron = new Player("Aaron");
        Player felpa = new Player("Felpa");
        TurnManager game = new TurnManager(aaron, felpa);

        // Play a Draw Two card
        DrawTwoCard drawTwoCard = new DrawTwoCard("red");
        aaron.getCards().add(drawTwoCard);
        game.playerPlayCard(aaron, drawTwoCard);

        // Check that Felpa drew two cards
        assertEquals(9, felpa.getCards().size()); // 7 initial + 2 drawn

        // The turn should move back to Aaron (Draw Two skips the next player)
        assertEquals(aaron, game.getCurrentPlayer());
    }

    @Test
    public void testSkipCardEffect() {
        Player aaron = new Player("Aaron");
        Player felpa = new Player("Felpa");
        Player proca = new Player("Proca");
        TurnManager game = new TurnManager(aaron, felpa, proca);

        // Play a Skip card
        SkipCard skipCard = new SkipCard("blue");
        aaron.getCards().add(skipCard);
        game.playerPlayCard(aaron, skipCard);

        // The next player should be Proca, skipping Felpa
        assertEquals(proca, game.getCurrentPlayer());
    }

    @Test
    public void testReverseCardEffect() {
        Player aaron = new Player("Aaron");
        Player felpa = new Player("Felpa");
        Player proca = new Player("Proca");
        TurnManager game = new TurnManager(aaron, felpa, proca);

        // Play a Reverse card
        ReverseCard reverseCard = new ReverseCard("yellow");
        aaron.getCards().add(reverseCard);
        game.playerPlayCard(aaron, reverseCard);

        // The turn order should be reversed, and the next player should be Proca
        assertEquals(proca, game.getCurrentPlayer());
    }

    @Test
    public void testWildcardColorSelection() {
        Player aaron = new Player("Aaron");
        Player felpa = new Player("Felpa");
        TurnManager game = new TurnManager(aaron, felpa);

        // Play a Wildcard
        Wildcard wildcard = new Wildcard();
        aaron.getCards().add(wildcard);
        aaron.setWildcardColor(wildcard, "red");
        game.playerPlayCard(aaron, wildcard);

        // Verify the last card played is the wildcard with the correct color
        assertEquals(wildcard, game.getLastCardPlayed());
        assertEquals("red", game.getLastCardPlayed().getColor());

        // The turn should move to the next player
        assertEquals(felpa, game.getCurrentPlayer());
    }

    @Test
    public void testPlayerVictory() {
        Player aaron = new Player("Aaron");
        Player felpa = new Player("Felpa");
        TurnManager game = new TurnManager(aaron, felpa);

        // Remove all but one card from Aaron's hand
        while (aaron.getCards().size() > 1) {
            aaron.getCards().removeFirst();
        }

        // Play the last card
        Card lastCard = aaron.getCards().getFirst();
        game.playerPlayCard(aaron, lastCard);

        // Aaron should win and the game should end
        assertEquals(0, aaron.getCards().size());
    }

    @Test
    public void testInvalidTurnPlay() {
        Player aaron = new Player("Aaron");
        Player felpa = new Player("Felpa");
        TurnManager game = new TurnManager(aaron, felpa);

        Card card = felpa.getCards().get(0);

        // Try to play a card out of turn
        assertThrows(RuntimeException.class, () -> game.playerPlayCard(felpa, card));
    }

    @Test
    public void testInvalidCardPlay() {
        Player aaron = new Player("Aaron");
        Player felpa = new Player("Felpa");
        TurnManager game = new TurnManager(aaron, felpa);

        // Create a card that Aaron doesn't have
        DrawTwoCard invalidCard = new DrawTwoCard("green");

        // Try to play an invalid card
        assertThrows(RuntimeException.class, () -> game.playerPlayCard(aaron, invalidCard));
    }

}
