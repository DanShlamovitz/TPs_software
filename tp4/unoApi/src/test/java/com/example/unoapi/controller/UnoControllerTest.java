package com.example.unoapi.controller;

import com.example.unoapi.model.*;
import com.example.unoapi.service.UnoService;
import com.example.unoapi.service.exceptions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UnoController.class)
class UnoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UnoService unoService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID testMatchId;
    private JsonCard testJsonCard;
    private Card testCard;
    private List<Card> testHand;

    @BeforeEach
    void setUp() {
        testMatchId = UUID.randomUUID();
        testJsonCard = new JsonCard("red", 5, "NumberCard", false);
        testCard = new NumberCard("red", 5);
        testHand = Arrays.asList(
            new NumberCard("red", 1),
            new NumberCard("blue", 2),
            new SkipCard("green")
        );
    }

    @Test
    void testNewMatch_Success() throws Exception {
        // Arrange
        when(unoService.newMatch(anyList())).thenReturn(testMatchId);

        // Act & Assert
        mockMvc.perform(post("/newmatch")
                .param("players", "Alice")
                .param("players", "Bob"))
                .andExpect(status().isOk())
                .andExpect(content().string("\"" + testMatchId.toString() + "\""));

        verify(unoService, times(1)).newMatch(anyList());
    }

    @Test
    void testNewMatch_InvalidCardException() throws Exception {
        // Arrange
        when(unoService.newMatch(anyList()))
                .thenThrow(new InvalidCardException("At least 2 players are required"));

        // Act & Assert
        mockMvc.perform(post("/newmatch")
                .param("players", "Alice"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Invalid Card"))
                .andExpect(jsonPath("$.message").value("Invalid card: At least 2 players are required"));

        verify(unoService, times(1)).newMatch(anyList());
    }

    @Test
    void testPlay_Success() throws Exception {
        // Arrange
        doNothing().when(unoService).play(any(UUID.class), anyString(), any(JsonCard.class));

        // Act & Assert
        mockMvc.perform(post("/play/{matchId}/{player}", testMatchId, "Alice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testJsonCard)))
                .andExpect(status().isOk());

        verify(unoService, times(1)).play(eq(testMatchId), eq("Alice"), any(JsonCard.class));
    }

    @Test
    void testPlay_MatchNotFoundException() throws Exception {
        // Arrange
        doThrow(new MatchNotFoundException(testMatchId.toString()))
                .when(unoService).play(any(UUID.class), anyString(), any(JsonCard.class));

        // Act & Assert
        mockMvc.perform(post("/play/{matchId}/{player}", testMatchId, "Alice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testJsonCard)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Match Not Found"));

        verify(unoService, times(1)).play(any(UUID.class), anyString(), any(JsonCard.class));
    }

    @Test
    void testPlay_NotPlayerTurnException() throws Exception {
        // Arrange
        doThrow(new NotPlayerTurnException("Alice"))
                .when(unoService).play(any(UUID.class), anyString(), any(JsonCard.class));

        // Act & Assert
        mockMvc.perform(post("/play/{matchId}/{player}", testMatchId, "Alice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testJsonCard)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.error").value("Not Player's Turn"));

        verify(unoService, times(1)).play(any(UUID.class), anyString(), any(JsonCard.class));
    }

    @Test
    void testPlay_CardNotInHandException() throws Exception {
        // Arrange
        doThrow(new CardNotInHandException("Alice"))
                .when(unoService).play(any(UUID.class), anyString(), any(JsonCard.class));

        // Act & Assert
        mockMvc.perform(post("/play/{matchId}/{player}", testMatchId, "Alice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testJsonCard)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Card Not in Hand"));

        verify(unoService, times(1)).play(any(UUID.class), anyString(), any(JsonCard.class));
    }

    @Test
    void testPlay_InvalidCardPlayException() throws Exception {
        // Arrange
        doThrow(new InvalidCardPlayException())
                .when(unoService).play(any(UUID.class), anyString(), any(JsonCard.class));

        // Act & Assert
        mockMvc.perform(post("/play/{matchId}/{player}", testMatchId, "Alice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testJsonCard)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Invalid Card Play"));

        verify(unoService, times(1)).play(any(UUID.class), anyString(), any(JsonCard.class));
    }

    @Test
    void testPlay_GameOverException() throws Exception {
        // Arrange
        doThrow(new GameOverException())
                .when(unoService).play(any(UUID.class), anyString(), any(JsonCard.class));

        // Act & Assert
        mockMvc.perform(post("/play/{matchId}/{player}", testMatchId, "Alice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testJsonCard)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Game Over"));

        verify(unoService, times(1)).play(any(UUID.class), anyString(), any(JsonCard.class));
    }

    @Test
    void testDrawCard_Success() throws Exception {
        // Arrange
        doNothing().when(unoService).drawCard(any(UUID.class), anyString());

        // Act & Assert
        mockMvc.perform(post("/draw/{matchId}/{player}", testMatchId, "Alice"))
                .andExpect(status().isOk());

        verify(unoService, times(1)).drawCard(eq(testMatchId), eq("Alice"));
    }

    @Test
    void testDrawCard_MatchNotFoundException() throws Exception {
        // Arrange
        doThrow(new MatchNotFoundException(testMatchId.toString()))
                .when(unoService).drawCard(any(UUID.class), anyString());

        // Act & Assert
        mockMvc.perform(post("/draw/{matchId}/{player}", testMatchId, "Alice"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Match Not Found"));

        verify(unoService, times(1)).drawCard(any(UUID.class), anyString());
    }

    @Test
    void testDrawCard_NotPlayerTurnException() throws Exception {
        // Arrange
        doThrow(new NotPlayerTurnException("Alice"))
                .when(unoService).drawCard(any(UUID.class), anyString());

        // Act & Assert
        mockMvc.perform(post("/draw/{matchId}/{player}", testMatchId, "Alice"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.error").value("Not Player's Turn"));

        verify(unoService, times(1)).drawCard(any(UUID.class), anyString());
    }

    @Test
    void testActiveCard_Success() throws Exception {
        // Arrange
        when(unoService.activeCard(any(UUID.class))).thenReturn(testCard);

        // Act & Assert
        mockMvc.perform(get("/activecard/{matchId}", testMatchId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.color").value("red"))
                .andExpect(jsonPath("$.number").value(5))
                .andExpect(jsonPath("$.type").value("NumberCard"))
                .andExpect(jsonPath("$.shout").value(false));

        verify(unoService, times(1)).activeCard(eq(testMatchId));
    }

    @Test
    void testActiveCard_MatchNotFoundException() throws Exception {
        // Arrange
        when(unoService.activeCard(any(UUID.class)))
                .thenThrow(new MatchNotFoundException(testMatchId.toString()));

        // Act & Assert
        mockMvc.perform(get("/activecard/{matchId}", testMatchId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Match Not Found"));

        verify(unoService, times(1)).activeCard(any(UUID.class));
    }

    @Test
    void testPlayerHand_Success() throws Exception {
        // Arrange
        when(unoService.playerHand(any(UUID.class))).thenReturn(testHand);

        // Act & Assert
        mockMvc.perform(get("/playerhand/{matchId}", testMatchId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].color").value("red"))
                .andExpect(jsonPath("$[0].number").value(1))
                .andExpect(jsonPath("$[1].color").value("blue"))
                .andExpect(jsonPath("$[1].number").value(2))
                .andExpect(jsonPath("$[2].type").value("SkipCard"));

        verify(unoService, times(1)).playerHand(eq(testMatchId));
    }

    @Test
    void testPlayerHand_MatchNotFoundException() throws Exception {
        // Arrange
        when(unoService.playerHand(any(UUID.class)))
                .thenThrow(new MatchNotFoundException(testMatchId.toString()));

        // Act & Assert
        mockMvc.perform(get("/playerhand/{matchId}", testMatchId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Match Not Found"));

        verify(unoService, times(1)).playerHand(any(UUID.class));
    }

    @Test
    void testGenericException() throws Exception {
        // Arrange
        when(unoService.activeCard(any(UUID.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        mockMvc.perform(get("/activecard/{matchId}", testMatchId))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred: Unexpected error"));

        verify(unoService, times(1)).activeCard(any(UUID.class));
    }
} 