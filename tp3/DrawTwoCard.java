package uno;

public class DrawTwoCard extends Card{
    public DrawTwoCard(String color) { super(color, "Draw Two"); }
    public void play(TurnManager turnManager) {
        turnManager.setLastCardPlayed(this);

        int skippedPlayerIndex = (turnManager.getCurrentPlayerIndex() + 1)
                % turnManager.getNumberOfPlayers();

        int nextPlayerIndex = (turnManager.getCurrentPlayerIndex() + 2)
                % turnManager.getNumberOfPlayers();

        turnManager.getPlayers().get(skippedPlayerIndex).drawCards(turnManager.getDeck(), 2);

        turnManager.setCurrentPlayer(turnManager.getPlayers().get(nextPlayerIndex));
    }
}