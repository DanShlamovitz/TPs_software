package uno;

public class SkipCard extends Card {
    public SkipCard(String color) { super(color, "Skip"); }

    public TurnManager play(TurnManager turnManager) {
        turnManager.setLastCardPlayed(this);

        int nextPlayerIndex = (turnManager.getCurrentPlayerIndex() + 2)
                % turnManager.getNumberOfPlayers();

        return turnManager.setCurrentPlayer(turnManager.getPlayers().get(nextPlayerIndex));
    }
}
