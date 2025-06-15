package com.example.unoapi.service;

import com.example.unoapi.model.Match;
import com.example.unoapi.model.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UnoService {
    @Autowired
    private Dealer dealer;
    private Map<UUID, Match> sessions = new HashMap<UUID, Match>();

    public UUID newMatch(List<String> players) {
        UUID newKey = UUID.randomUUID();
        sessions.put(newKey, Match.fullMatch(dealer.fullDeck(), players));
        return newKey;
    }

    public List<Card> playerHand(UUID matchId) {
        Match match = sessions.get(matchId);
        if (match == null) {
            throw new IllegalArgumentException("Match not found");
        }
        return match.playerHand();
    }

    public Card activeCard(UUID matchId) {
        Match match = sessions.get(matchId);
        if (match == null) {
            throw new IllegalArgumentException("Match not found");
        }
        return match.activeCard();
    }

    public void drawCard(UUID matchId, String player) {
        Match match = sessions.get(matchId);
        if (match == null) {
            throw new IllegalArgumentException("Match not found");
        }
        match.drawCard(player);
    }

    public void play(UUID matchId, String player, Card card) {
        Match match = sessions.get(matchId);
        if (match == null) {
            throw new IllegalArgumentException("Match not found");
        }
        match.play(player, card);
    }
}
