package uno;

public class Wildcard extends Card{
    public Wildcard() {
        this.color = "Any";
    }
    public Wildcard setColor(String color) {
        this.color = color;
        return this;
    }
    public Game play(Game game) {
        return game.playWildcard(this);
    }
    public boolean canReceiveCard(Card card) {
        return likesColor(card.getColor()) || this.getClass().equals(card.getClass());
    }
    public boolean canBeStackedOnTopOf(Card card) {
        return true;
    }
    public boolean likesNumber(int number) {
        return false;
    }
    public boolean equals(Card card) {
        return this.getClass().equals(card.getClass());
    }
}