package com.example.unoapi.service;

import com.example.unoapi.model.*;
import com.example.unoapi.service.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnoServiceTest {

    @Mock
    private Dealer dealer;

    @Mock
    private CardConverter cardConverter;

    @InjectMocks
    private UnoService unoService;

    private List<Card> mockDeck;
    private JsonCard mockJsonCard;
    private Card mockCard;

    @BeforeEach
    void setUp() {
        mockDeck = Arrays.asList(
            // Carta inicial para descarte
            new NumberCard("red", 5),
            // 7 cartas para primer jugador
            new NumberCard("blue", 3),
            new SkipCard("green"),
            new Draw2Card("yellow"),
            new ReverseCard("red"),
            new WildCard(),
            new NumberCard("blue", 7),
            new NumberCard("green", 2),
            // 7 cartas para segundo jugador
            new NumberCard("yellow", 8),
            new NumberCard("red", 1),
            new NumberCard("blue", 4),
            new NumberCard("green", 6),
            new NumberCard("yellow", 9),
            new NumberCard("red", 0),
            new SkipCard("blue"),
            // Cartas extras para draws y tests adicionales
            new NumberCard("green", 3),
            new NumberCard("yellow", 4),
            new Draw2Card("red"),
            new ReverseCard("blue"),
            new WildCard()
        );

        mockJsonCard = new JsonCard("red", 5, "NumberCard", false);
        mockCard = new NumberCard("red", 5);
    }

    @Test
    void testNewMatch_Success() {
        // Arrange
        List<String> players = Arrays.asList("Alice", "Bob");
        when(dealer.fullDeck()).thenReturn(mockDeck);

        // Act
        UUID matchId = unoService.newMatch(players);

        // Assert
        assertNotNull(matchId);
        verify(dealer, times(1)).fullDeck();
    }

    @Test
    void testNewMatch_NullPlayers_ThrowsException() {
        // Act & Assert
        assertThrows(InvalidCardException.class, () -> {
            unoService.newMatch(null);
        });
    }

    @Test
    void testNewMatch_EmptyPlayers_ThrowsException() {
        // Act & Assert
        assertThrows(InvalidCardException.class, () -> {
            unoService.newMatch(Arrays.asList());
        });
    }

    @Test
    void testNewMatch_OnlyOnePlayer_ThrowsException() {
        // Act & Assert
        assertThrows(InvalidCardException.class, () -> {
            unoService.newMatch(Arrays.asList("Alice"));
        });
    }

    @Test
    void testPlayerHand_Success() {
        // Arrange
        List<String> players = Arrays.asList("Alice", "Bob");
        when(dealer.fullDeck()).thenReturn(mockDeck);
        UUID matchId = unoService.newMatch(players);

        // Act
        List<Card> hand = unoService.playerHand(matchId);

        // Assert
        assertNotNull(hand);
        assertEquals(7, hand.size()); // Full match gives 7 cards
    }

    @Test
    void testPlayerHand_MatchNotFound_ThrowsException() {
        // Arrange
        UUID nonExistentMatchId = UUID.randomUUID();

        // Act & Assert
        assertThrows(MatchNotFoundException.class, () -> {
            unoService.playerHand(nonExistentMatchId);
        });
    }

    @Test
    void testActiveCard_Success() {
        // Arrange
        List<String> players = Arrays.asList("Alice", "Bob");
        when(dealer.fullDeck()).thenReturn(mockDeck);
        UUID matchId = unoService.newMatch(players);

        // Act
        Card activeCard = unoService.activeCard(matchId);

        // Assert
        assertNotNull(activeCard);
    }

    @Test
    void testActiveCard_MatchNotFound_ThrowsException() {
        // Arrange
        UUID nonExistentMatchId = UUID.randomUUID();

        // Act & Assert
        assertThrows(MatchNotFoundException.class, () -> {
            unoService.activeCard(nonExistentMatchId);
        });
    }

    @Test
    void testPlay_Success() {
        // Arrange
        List<String> players = Arrays.asList("Alice", "Bob");
        when(dealer.fullDeck()).thenReturn(mockDeck);
        when(cardConverter.jsonToCard(mockJsonCard)).thenReturn(mockCard);
        
        UUID matchId = unoService.newMatch(players);

        // Act & Assert (no exception should be thrown)
        assertDoesNotThrow(() -> {
            unoService.play(matchId, "Alice", mockJsonCard);
        });

        verify(cardConverter, times(1)).jsonToCard(mockJsonCard);
    }

    @Test
    void testPlay_MatchNotFound_ThrowsException() {
        // Arrange
        UUID nonExistentMatchId = UUID.randomUUID();

        // Act & Assert
        assertThrows(MatchNotFoundException.class, () -> {
            unoService.play(nonExistentMatchId, "Alice", mockJsonCard);
        });
    }

    @Test
    void testPlay_NullPlayer_ThrowsException() {
        // Arrange
        List<String> players = Arrays.asList("Alice", "Bob");
        when(dealer.fullDeck()).thenReturn(mockDeck);
        UUID matchId = unoService.newMatch(players);

        // Act & Assert
        assertThrows(InvalidCardException.class, () -> {
            unoService.play(matchId, null, mockJsonCard);
        });
    }

    @Test
    void testPlay_EmptyPlayer_ThrowsException() {
        // Arrange
        List<String> players = Arrays.asList("Alice", "Bob");
        when(dealer.fullDeck()).thenReturn(mockDeck);
        UUID matchId = unoService.newMatch(players);

        // Act & Assert
        assertThrows(InvalidCardException.class, () -> {
            unoService.play(matchId, "", mockJsonCard);
        });
    }

    @Test
    void testPlay_NullCard_ThrowsException() {
        // Arrange
        List<String> players = Arrays.asList("Alice", "Bob");
        when(dealer.fullDeck()).thenReturn(mockDeck);
        UUID matchId = unoService.newMatch(players);

        // Act & Assert
        assertThrows(InvalidCardException.class, () -> {
            unoService.play(matchId, "Alice", null);
        });
    }

    @Test
    void testDrawCard_Success() {
        // Arrange
        List<String> players = Arrays.asList("Alice", "Bob");
        when(dealer.fullDeck()).thenReturn(mockDeck);
        UUID matchId = unoService.newMatch(players);

        // Act & Assert (no exception should be thrown)
        assertDoesNotThrow(() -> {
            unoService.drawCard(matchId, "Alice");
        });
    }

    @Test
    void testDrawCard_MatchNotFound_ThrowsException() {
        // Arrange
        UUID nonExistentMatchId = UUID.randomUUID();

        // Act & Assert
        assertThrows(MatchNotFoundException.class, () -> {
            unoService.drawCard(nonExistentMatchId, "Alice");
        });
    }

    @Test
    void testDrawCard_NullPlayer_ThrowsException() {
        // Arrange
        List<String> players = Arrays.asList("Alice", "Bob");
        when(dealer.fullDeck()).thenReturn(mockDeck);
        UUID matchId = unoService.newMatch(players);

        // Act & Assert
        assertThrows(InvalidCardException.class, () -> {
            unoService.drawCard(matchId, null);
        });
    }

    @Test
    void testDrawCard_EmptyPlayer_ThrowsException() {
        // Arrange
        List<String> players = Arrays.asList("Alice", "Bob");
        when(dealer.fullDeck()).thenReturn(mockDeck);
        UUID matchId = unoService.newMatch(players);

        // Act & Assert
        assertThrows(InvalidCardException.class, () -> {
            unoService.drawCard(matchId, "");
        });
    }

    @Test
    void testGetMatchOrThrow_NullMatchId_ThrowsException() {
        // Act & Assert
        assertThrows(MatchNotFoundException.class, () -> {
            unoService.playerHand(null);
        });
    }
} 