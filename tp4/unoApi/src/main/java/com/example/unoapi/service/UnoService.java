package com.example.unoapi.service;

import com.example.unoapi.model.Match;
import com.example.unoapi.model.Card;
import com.example.unoapi.model.JsonCard;
import com.example.unoapi.model.NumberCard;
import com.example.unoapi.model.SkipCard;
import com.example.unoapi.model.Draw2Card;
import com.example.unoapi.model.ReverseCard;
import com.example.unoapi.model.WildCard;
import com.example.unoapi.service.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UnoService {
    private Map<UUID, Match> sessions = new HashMap<UUID, Match>();

    @Autowired
    private Dealer dealer;

    @Autowired
    private CardConverter cardConverter;

    public static List<Card> fullDeck() {
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
            deck.add(new WildCard());
        }
        
        return deck; // Sin mezclar
    }

    public UUID newMatch(List<String> players) {
        UUID newKey = UUID.randomUUID();
        sessions.put(newKey, Match.fullMatch(dealer.fullDeck(), players));
        return newKey;
    }

    public List<Card> playerHand(UUID matchId) {
        Match match = sessions.get(matchId);
        return match.playerHand();
    }

    public Card activeCard(UUID matchId) {
        Match match = sessions.get(matchId);
        return match.activeCard();
    }

    public void play(UUID matchId, String player, JsonCard card) {
        Match match = sessions.get(matchId);
        Card cardToPlay = cardConverter.jsonToCard(card);
        match.play(player, cardToPlay);
    }

    public void drawCard(UUID matchId, String player) {
        Match match = sessions.get(matchId);
        match.drawCard(player);
    }
}
