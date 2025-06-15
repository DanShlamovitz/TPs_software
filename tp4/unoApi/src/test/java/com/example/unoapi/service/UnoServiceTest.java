package com.example.unoapi.service;

import com.example.unoapi.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UnoServiceTest {
    @Autowired
    private UnoService unoService;
    
    private UUID testMatchId;
    private List<String> gamePlayers;

    @BeforeEach
    void setUp() {
        gamePlayers = List.of("Alice", "Bob", "Charlie");
        testMatchId = unoService.newMatch(gamePlayers);
    }

    @Test 
    public void playerHandHasCorrectSize() {
        List<Card> hand = unoService.playerHand(testMatchId);
        assertEquals(7, hand.size());
    }

    @Test
    public void newMatchGeneratesUUID() {
        UUID id = unoService.newMatch(List.of("Player1", "Player2"));
        assertNotNull(id);
    }

    @Test
    public void activeCardExists() {
        Card activeCard = unoService.activeCard(testMatchId);
        assertNotNull(activeCard);
    }

    @Test
    public void drawCardIncreasesHandSize() {
        int initialSize = unoService.playerHand(testMatchId).size();
        unoService.drawCard(testMatchId, "Alice");
        int newSize = unoService.playerHand(testMatchId).size();
        assertEquals(initialSize + 1, newSize);
    }

    @Test
    public void newMatchWithSinglePlayerWorks() {
        UUID id = unoService.newMatch(List.of("SoloPlayer"));
        assertNotNull(id);
        List<Card> hand = unoService.playerHand(id);
        assertEquals(7, hand.size());
    }

    @Test
    public void invalidMatchThrowsException() {
        UUID invalidId = UUID.randomUUID();
        assertThrows(IllegalArgumentException.class, () -> {
            unoService.playerHand(invalidId);
        });
    }

    @Test
    public void nullMatchIdThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            unoService.activeCard(null);
        });
    }

    @Test
    public void playValidCardDoesNotThrow() {
        List<Card> hand = unoService.playerHand(testMatchId);
        Card cardToPlay = hand.get(0);
        
        assertDoesNotThrow(() -> {
            unoService.play(testMatchId, "Alice", cardToPlay);
        });
    }

    @Test
    public void activeCardRemainsConsistent() {
        Card card1 = unoService.activeCard(testMatchId);
        Card card2 = unoService.activeCard(testMatchId);
        assertEquals(card1, card2);
    }

    @Test
    public void playerHandConsistency() {
        List<Card> hand1 = unoService.playerHand(testMatchId);
        List<Card> hand2 = unoService.playerHand(testMatchId);
        assertEquals(hand1.size(), hand2.size());
    }

    @Test
    public void drawCardFromInvalidMatchFails() {
        UUID fakeId = UUID.randomUUID();
        assertThrows(IllegalArgumentException.class, () -> {
            unoService.drawCard(fakeId, "Alice");
        });
    }

    @Test
    public void playWithInvalidMatchFails() {
        UUID fakeId = UUID.randomUUID();
        Card card = new NumberCard("Red", 5);
        assertThrows(IllegalArgumentException.class, () -> {
            unoService.play(fakeId, "Alice", card);
        });
    }

    @Test
    public void multipleMatchesWork() {
        UUID match1 = unoService.newMatch(List.of("P1", "P2"));
        UUID match2 = unoService.newMatch(List.of("P3", "P4"));
        
        assertNotEquals(match1, match2);
        assertNotNull(unoService.activeCard(match1));
        assertNotNull(unoService.activeCard(match2));
    }

    @Test
    public void drawCardWithInvalidPlayerFails() {
        assertThrows(RuntimeException.class, () -> {
            unoService.drawCard(testMatchId, "NonExistentPlayer");
        });
    }

    @Test
    public void activeCardNotInPlayerHand() {
        Card activeCard = unoService.activeCard(testMatchId);
        List<Card> playerHand = unoService.playerHand(testMatchId);
        assertFalse(playerHand.contains(activeCard));
    }

    @Test
    public void playWithInvalidPlayerThrowsException() {
        Card card = new NumberCard("Blue", 3);
        assertThrows(RuntimeException.class, () -> {
            unoService.play(testMatchId, "InvalidPlayer", card);
        });
    }

    @Test
    public void newMatchWithMultiplePlayersWorks() {
        UUID id = unoService.newMatch(List.of("A", "B", "C", "D", "E"));
        assertNotNull(id);
        assertEquals(7, unoService.playerHand(id).size());
    }

    @Test
    public void drawCardTwiceIncreasesHandByTwo() {
        int initialSize = unoService.playerHand(testMatchId).size();
        unoService.drawCard(testMatchId, "Alice");
        unoService.drawCard(testMatchId, "Alice");
        int finalSize = unoService.playerHand(testMatchId).size();
        assertEquals(initialSize + 2, finalSize);
    }
} 