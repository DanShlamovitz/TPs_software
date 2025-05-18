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
    public boolean hasCard(Card card) {
        for (Card c : cards) {
            if (card == c) {
                return true;
            }
        }
        return false;
    }
    public void setWildcardColor(Wildcard wildcard, String color) {
        if ( !hasCard(wildcard) ) {
            throw new RuntimeException("The requested wildcard doesn't belong to this player!");
        }
        wildcard.setColor(color);
    }
    public Player drawCards(Deck deck, int nOfCards) {
        cards.addAll(deck.draw(nOfCards));
        return this;
    }
    public String getName() { return name; }
    public ArrayList<Card> getCards() { return cards; }
}
