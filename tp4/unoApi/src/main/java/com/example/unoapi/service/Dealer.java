package com.example.unoapi.service;

import com.example.unoapi.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class Dealer {

    public List<Card> fullDeck() {
        ArrayList<Card> deck = new ArrayList<>();
        
        // Add colored cards for each color (red, blue, green, yellow)
        String[] colors = {"red", "blue", "green", "yellow"};
        
        for (String color : colors) {
            // Add one "0" card per color
            deck.add(new NumberCard(color, 0));
            
            // Add two cards of each number 1-9 per color
            for (int number = 1; number <= 9; number++) {
                deck.add(new NumberCard(color, number));
                deck.add(new NumberCard(color, number));
            }
            
            // Add two action cards of each type per color
            deck.add(new SkipCard(color));
            deck.add(new SkipCard(color));
            
            deck.add(new Draw2Card(color));
            deck.add(new Draw2Card(color));
            
            deck.add(new ReverseCard(color));
            deck.add(new ReverseCard(color));
        }
        
        // Add 4 Wild cards
        for (int i = 0; i < 4; i++) {
            deck.add(new WildCard());
        }
        
        // Add 4 Wild Draw Four cards (using WildCard for now)
        for (int i = 0; i < 4; i++) {
            deck.add(new WildCard()); // TODO: Implement WildDraw4Card if needed
        }
        
        return deck; // Sin mezclar
    }

    public List<Card> shuffledDeck() {
        ArrayList<Card> deck = new ArrayList<>();
        
        // Add colored cards for each color (red, blue, green, yellow)
        String[] colors = {"red", "blue", "green", "yellow"};
        
        for (String color : colors) {
            // Add one "0" card per color
            deck.add(new NumberCard(color, 0));
            
            // Add two cards of each number 1-9 per color
            for (int number = 1; number <= 9; number++) {
                deck.add(new NumberCard(color, number));
                deck.add(new NumberCard(color, number));
            }
            
            // Add two action cards of each type per color
            deck.add(new SkipCard(color));
            deck.add(new SkipCard(color));
            
            deck.add(new Draw2Card(color));
            deck.add(new Draw2Card(color));
            
            deck.add(new ReverseCard(color));
            deck.add(new ReverseCard(color));
        }
        
        // Add 4 Wild cards
        for (int i = 0; i < 4; i++) {
            deck.add(new WildCard());
        }
        
        // Add 4 Wild Draw Four cards (using WildCard for now)
        for (int i = 0; i < 4; i++) {
            deck.add(new WildCard()); // TODO: Implement WildDraw4Card if needed
        }
        
        Collections.shuffle(deck);
        return deck;
    }

    public List<Card> reducedDeck() {
        ArrayList<Card> deck = new ArrayList<>();
        String[] colors = {"red", "blue", "green", "yellow"};
        
        for (String color : colors) {
            // Reduced deck for testing - fewer cards per type
            deck.add(new NumberCard(color, 0));
            deck.add(new NumberCard(color, 1));
            deck.add(new NumberCard(color, 2));
            deck.add(new NumberCard(color, 3));
            deck.add(new SkipCard(color));
            deck.add(new Draw2Card(color));
            deck.add(new ReverseCard(color));
        }
        
        deck.add(new WildCard());
        deck.add(new WildCard());
        
        Collections.shuffle(deck);
        return deck;
    }

    public int getDeckSize() {
        return 108; // Standard UNO deck size
    }

    public int getReducedDeckSize() {
        return 30; // Reduced deck for testing
    }
}
