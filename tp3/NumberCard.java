package uno;

public class NumberCard extends Card{
    public NumberCard(String color, String symbol) { super(color, symbol); }
    public void play(TurnManager turnManager) {
        turnManager.setLastCardPlayed(this);

        int nextPlayerIndex = (turnManager.getCurrentPlayerIndex() + 1) %
                turnManager.getNumberOfPlayers();

        turnManager.setCurrentPlayer(turnManager.getPlayers().get(nextPlayerIndex));
    }
}
