package uno;

public class Wildcard extends Card{
    public Wildcard() { super("Any", "Wildcard"); }
    public Wildcard setColor(String color) {
        this.color = color;
        return this;
    }
    public void play(TurnManager turnManager) {
        if (color.equals("Any")) {
            throw new RuntimeException("A Wildcard's color must be set before it is played!");
        }
        // PREGUNTARLE A EMILIO SI ESTO LE RESULTA CODIGO REPETIDO QUE HAY QUE ELIMINAR
        turnManager.setLastCardPlayed(this);
        int nextPlayerIndex = (turnManager.getCurrentPlayerIndex() + 1) %
                turnManager.getNumberOfPlayers();

        turnManager.setCurrentPlayer(turnManager.getPlayers().get(nextPlayerIndex));
    }
}