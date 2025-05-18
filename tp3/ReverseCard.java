package uno;

import java.util.Collections;

public class ReverseCard extends Card {
    public ReverseCard(String color) { super(color, "Reverse"); }

    public TurnManager play(TurnManager turnManager) {
        turnManager.setLastCardPlayed(this);
        Collections.reverse(turnManager.getPlayers());

        int nextPlayerIndex = (turnManager.getCurrentPlayerIndex() + 1)
                % turnManager.getNumberOfPlayers();
        return turnManager.setCurrentPlayer(turnManager.getPlayers().get(nextPlayerIndex));
    }
}
