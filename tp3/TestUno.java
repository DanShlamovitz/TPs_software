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

        TurnManager newGame = aaron.playCard(aaron.getCards().getFirst(), game);
        assertEquals(aaron.getCards().size(), 6);

        assertEquals(n`ewGame.getCurrentPlayer(), felpa);

    }
}
