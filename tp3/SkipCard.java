package uno;

public class SkipCard extends Card {
    public SkipCard(String color) {
        this.color = color;
    }
    public Game play(Game game) {
        return game.playSkipCard(this);
    }
    public boolean canReceiveCard(Card card) {
        return likesColor(card.getColor()) || this.getClass().equals(card.getClass());
    }
    public boolean canBeStackedOnTopOf(Card card) {
        return card.canReceiveCard(this);
    }
    public boolean likesNumber(int number) {
        return false;
    }
    public boolean equals(Card card) {
        return likesColor(card.getColor()) && this.getClass().equals(card.getClass());
    }
}