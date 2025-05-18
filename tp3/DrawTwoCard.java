package uno;

public class DrawTwoCard extends Card{
    public DrawTwoCard(String color) { super(color, "Draw Two"); }
    public TurnManager play(TurnManager turnManager) {
        turnManager.setLastCardPlayed(this);

        int nextPlayerIndex = (turnManager.getCurrentPlayerIndex() + 2)
                % turnManager.getNumberOfPlayers();

        turnManager.getPlayers().get(nextPlayerIndex).drawCards(turnManager.getDeck(), 2);

        return turnManager.setCurrentPlayer(turnManager.getPlayers().get(nextPlayerIndex));
    }
}