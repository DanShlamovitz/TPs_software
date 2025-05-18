package uno;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Player {

    private ArrayList<Card> cards = new ArrayList<>();
    private String name;

    public Player(String name) { this.name = name; }

    public void checkForVictory() {
        if (cards.size() == 0) {
            System.out.println(this.name + " won the game!");
            System.exit(0);
        }
    }
    public TurnManager playCard(Card card, TurnManager turnManager) {
        if ( !(turnManager.getCurrentPlayer().getName().equals(this.name)) ) {
            throw new RuntimeException("Error: this player can't play at the moment because" +
                    " it isn't their turn yet!");
        }
        if (!cards.contains(card)) {
            throw new RuntimeException("Error: invalid card was played." +
                    " The player doesn't have the requested card in their hand!");
        }
        if (card.canBePlayedWith(turnManager.getLastCardPlayed())) {
            cards.remove(card);
            checkForVictory();
            return turnManager.playCard(card);
        }
        throw new RuntimeException("Error: invalid card was played. The selected card" +
                " can't be played after the last card that was played!");
    }
    public Player drawCards(Deck deck, int nOfCards) {
        cards.addAll(deck.draw(nOfCards));
        return this;
    }
    public String getName() { return name; }
    public ArrayList<Card> getCards() { return cards; }
}
