package uno;

public class NumberCard extends Card{
    public NumberCard(String color, String symbol) { super(color, symbol); }
    public TurnManager play(TurnManager turnManager) {
        turnManager.setLastCardPlayed(this);

        int nextPlayerIndex = (turnManager.getCurrentPlayerIndex() + 1) %
                turnManager.getNumberOfPlayers();

        return turnManager.setCurrentPlayer(turnManager.getPlayers().get(nextPlayerIndex));
    }
}
