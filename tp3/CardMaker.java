package uno;

public class CardMaker {

    public Card makeCard(String color, String type) {
        if (type.matches("\\d")) { return new NumberCard(color, type); }
        if (type.equals("Reverse")) { return new ReverseCard(color); }
        if (type.equals("Skip")) { return new SkipCard(color); }
        if (type.equals("Wildcard")) { return new Wildcard(); }
        if (type.equals("Draw 2")) { return new DrawTwoCard(color); }
        throw new RuntimeException("Error: Invalid card type was passed to the makeCard function.");
    }
}
