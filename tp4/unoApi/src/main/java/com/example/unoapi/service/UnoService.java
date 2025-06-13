package com.example.unoapi.service;

import com.example.unoapi.model.Match;
import com.example.unoapi.model.Card;
import com.example.unoapi.model.JsonCard;
import com.example.unoapi.service.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public UUID newMatch(List<String> players) {
        if (players == null || players.isEmpty()) {
            throw new InvalidCardException("Players list cannot be null or empty");
        }
        if (players.size() < 2) {
            throw new InvalidCardException("At least 2 players are required");
        }
        
        UUID newKey = UUID.randomUUID();
        sessions.put(newKey, Match.fullMatch(dealer.fullDeck(), players));
        return newKey;
    }

    public List<Card> playerHand(UUID matchId) {
        Match match = getMatchOrThrow(matchId);
        return match.playerHand();
    }

    public Card activeCard(UUID matchId) {
        Match match = getMatchOrThrow(matchId);
        return match.activeCard();
    }

    public void play(UUID matchId, String player, JsonCard card) {
        Match match = getMatchOrThrow(matchId);
        
        if (player == null || player.trim().isEmpty()) {
            throw new InvalidCardException("Player name cannot be null or empty");
        }
        
        if (card == null) {
            throw new InvalidCardException("Card cannot be null");
        }
        
        try {
            Card cardToPlay = cardConverter.jsonToCard(card);
            match.play(player, cardToPlay);
        } catch (RuntimeException e) {
            handleMatchException(e, player);
        }
    }

    public void drawCard(UUID matchId, String player) {
        Match match = getMatchOrThrow(matchId);
        
        if (player == null || player.trim().isEmpty()) {
            throw new InvalidCardException("Player name cannot be null or empty");
        }
        
        try {
            match.drawCard(player);
        } catch (RuntimeException e) {
            handleMatchException(e, player);
        }
    }

    private Match getMatchOrThrow(UUID matchId) {
        if (matchId == null) {
            throw new MatchNotFoundException("null");
        }
        
        Match match = sessions.get(matchId);
        if (match == null) {
            throw new MatchNotFoundException(matchId.toString());
        }
        return match;
    }

    private void handleMatchException(RuntimeException e, String player) {
        String message = e.getMessage();
        
        if (message.contains("It is not turn of player")) {
            throw new NotPlayerTurnException(player);
        } else if (message.contains("Not a card in hand of")) {
            throw new CardNotInHandException(player);
        } else if (message.contains("Card does not match Color, Number or Kind")) {
            throw new InvalidCardPlayException();
        } else if (message.contains("GameOver")) {
            throw new GameOverException();
        } else {
            // Re-throw as generic UnoGameException if we can't classify it
            throw new InvalidCardException(message, e);
        }
    }
}
