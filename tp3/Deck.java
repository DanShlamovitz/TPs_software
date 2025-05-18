package uno;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Deck {
    ArrayList<Card> cards = new ArrayList<>();
    ArrayList<String> colors = new ArrayList<>(
            Arrays.asList("blue", "green", "yellow", "red")
    );
    ArrayList<String> types = new ArrayList<>(
            Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                    "1", "2", "3", "4", "5", "6", "7", "8", "9",
                    "Draw 2", "Wildcard", "Reverse", "Skip",
                    "Draw 2", "Wildcard", "Reverse", "Skip")
    );
    public Deck() {
        CardMaker cardMaker = new CardMaker();
        for (String color : colors) {
            for (String type : types) {
                cards.add( cardMaker.makeCard(color, type) );
            }
        }
        Collections.shuffle(cards);
    }
    public ArrayList<Card> draw(int numberOfCards) {
        ArrayList<Card> newCards = new ArrayList<>();
        for (int i = 0; i < numberOfCards; i++) {
            newCards.add( cards.removeFirst() );
        }
        return newCards;
    }
}
