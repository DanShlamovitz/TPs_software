package com.example.unoapi.service;

import com.example.unoapi.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class Dealer {

    public List<Card> fullDeck() {
        ArrayList<Card> deck = new ArrayList<>();
        //deck.addAll(//cartas redd) y asi para todos los colores
        deck.addAll(cardsOn("red"));
        deck.addAll(cardsOn("blue"));
        deck.addAll(cardsOn("green"));
        deck.addAll(cardsOn("yellow"));
        Collections.shuffle(deck);
        return deck;
    }

    private List<Card> cardsOn(String color) {
        return List.of(new WildCard(), new SkipCard(color),
                        new Draw2Card(color), new ReverseCard(color),
                        new NumberCard(color, 1),
                        new NumberCard(color, 2),
                        new NumberCard(color, 3),
                        new NumberCard(color, 4),
                        new NumberCard(color, 5),
                        new NumberCard(color, 6));
    }
}
