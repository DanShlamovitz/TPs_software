package uno;

public abstract class Card {
    private String symbol;
    protected String color;
    public Card(String color, String symbol) {
        this.color = color;
        this.symbol = symbol;
    }
    public abstract void play(TurnManager turnManager);
    public String getSymbol() { return symbol; }
    public String getColor() { return color; }
    public boolean canBePlayedWith(Card card) {
        return this.symbol.equals(card.getSymbol()) || this.color.equals(card.getColor())
                || this.getSymbol().equals("Wildcard") || card.getSymbol().equals("NoCard");
    }
}