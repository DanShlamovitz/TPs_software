package uno;

import java.util.ArrayList;

public class Player {

    private ArrayList<Card> cards = new ArrayList<>();
    private String name;
    private boolean shoutedUno;

    public Player(String name) {
        this.name = name;
        shoutedUno = false;
    }
    public Player(String name, ArrayList<Card> cards) {
        this.name = name;
        shoutedUno = false;
        this.cards = cards;
    }
    public boolean checkForVictory() {
        if (cards.size() == 0) {
            System.out.println(this.name + " won the game!");
            return true;
        }
        return false;
    }
    public boolean hasCard(Card card) {
        return cards.stream().anyMatch(c -> c.equals(card));
    }
    public Player setWildcardColor(Wildcard wildcard, String color) {
        if ( !hasCard(wildcard) ) {
            throw new RuntimeException("This player has no wildcards!");
        }
        wildcard.setColor(color);
        return this;
    }
    public Player drawCard(Game game) {
        this.cards.add(game.draw());
        return this;
    }
    public Player shoutUno() {
        shoutedUno = true;
        return this;
    }
    public Player resetUnoShout() {
        shoutedUno = false;
        return this;
    }
    public String getName() { return name; }
    public ArrayList<Card> getCards() { return cards; }
    public boolean shoutedUno() { return shoutedUno; }
}
