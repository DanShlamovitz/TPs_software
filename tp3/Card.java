package uno;

public abstract class Card {
    protected String color;
    public abstract Game play(Game turnManager);
    public String getColor() { return color; }
    public abstract boolean likesNumber(int number);
    public abstract boolean canBeStackedOnTopOf(Card card);
    public abstract boolean canReceiveCard(Card card);
    public abstract boolean equals(Card card);
    public boolean likesColor(String otherCardColor) {
        return color.equals(otherCardColor);
    }
}