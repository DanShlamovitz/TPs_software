package uno;

public class NoCard extends Card {

    public NoCard() { super("NoCard", "NoCard"); }
    public TurnManager play(TurnManager turnManager) {
        throw new RuntimeException("The NoCard isn't meant to be played!");
    }
    public boolean canBePlayedWith(Card card) { return false; }
}
