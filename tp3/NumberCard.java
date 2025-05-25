package uno;

public class NumberCard extends Card{
    private int number;
    public NumberCard(String color, int number) {
        this.number = number;
        this.color = color;
    }
    public Game play(Game game) {
        return game.playNumberCard(this);
    }
    public boolean likesNumber(int number) {
        return this.number == number;
    }
    public boolean canBeStackedOnTopOf(Card card) {
        return card.canReceiveCard(this);
    }
    public boolean canReceiveCard(Card card) {
        return card.likesNumber(this.number) || card.getColor().equals(this.getColor());
    }
    public boolean equals(Card card) {
        return card.likesNumber(this.number) && card.likesColor(this.getColor())
                && card.getClass().equals(this.getClass());
    }
}