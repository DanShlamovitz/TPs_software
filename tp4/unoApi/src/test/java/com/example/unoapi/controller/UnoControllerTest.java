package com.example.unoapi.controller;

import com.example.unoapi.service.UnoService;
import com.example.unoapi.model.JsonCard;
import com.example.unoapi.model.Card;
import com.example.unoapi.model.NumberCard;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UnoControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UnoService unoService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private UUID testMatchId;
    private String testPlayer;

    @BeforeEach
    void setUp() {
        testMatchId = UUID.randomUUID();
        testPlayer = "Player1";
    }

    @Test
    public void activeCardReturnsJsonCard() throws Exception {
        Card mockCard = new NumberCard("Blue", 3);
        when(unoService.activeCard(testMatchId)).thenReturn(mockCard);

        mockMvc.perform(get("/activecard/{matchId}", testMatchId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.color").value("Blue"))
                .andExpect(jsonPath("$.number").value(3));
    }

    @Test
    public void newMatchWithTwoPlayers() throws Exception {
        UUID expectedId = UUID.randomUUID();
        when(unoService.newMatch(any(List.class))).thenReturn(expectedId);

        mockMvc.perform(post("/newmatch")
                .param("players", "Alice", "Bob"))
                .andExpect(status().isOk())
                .andExpect(content().string("\"" + expectedId.toString() + "\""));
    }

    @Test
    public void drawCardIncrementsHand() throws Exception {
        mockMvc.perform(post("/draw/{matchId}/{player}", testMatchId, testPlayer))
                .andExpect(status().isOk());

        verify(unoService).drawCard(testMatchId, testPlayer);
    }

    @Test
    public void playCardWithValidData() throws Exception {
        JsonCard jsonCard = new JsonCard("Green", 7, "NumberCard", false);

        mockMvc.perform(post("/play/{matchId}/{player}", testMatchId, testPlayer)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(jsonCard)))
                .andExpect(status().isOk());

        verify(unoService).play(eq(testMatchId), eq(testPlayer), any(Card.class));
    }

    @Test
    public void playerHandReturnsCards() throws Exception {
        List<Card> mockHand = List.of(
            new NumberCard("Red", 5),
            new NumberCard("Yellow", 8)
        );
        when(unoService.playerHand(testMatchId)).thenReturn(mockHand);

        mockMvc.perform(get("/playerhand/{matchId}", testMatchId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void newMatchWithFourPlayers() throws Exception {
        UUID expectedId = UUID.randomUUID();
        when(unoService.newMatch(any(List.class))).thenReturn(expectedId);

        mockMvc.perform(post("/newmatch")
                .param("players", "A", "B", "C", "D"))
                .andExpect(status().isOk());
    }

    @Test
    public void exceptionHandlerReturns500() throws Exception {
        doThrow(new IllegalArgumentException("Test error")).when(unoService)
                .drawCard(any(UUID.class), anyString());

        mockMvc.perform(post("/draw/{matchId}/{player}", testMatchId, testPlayer))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Test error"));
    }

    @Test
    public void playInvalidJsonFormat() throws Exception {
        mockMvc.perform(post("/play/{matchId}/{player}", testMatchId, testPlayer)
                .contentType(MediaType.APPLICATION_JSON)
                .content("invalid json format"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void activeCardWithInvalidUUID() throws Exception {
        mockMvc.perform(get("/activecard/{matchId}", "not-a-valid-uuid"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void wrongMethodForActiveCard() throws Exception {
        mockMvc.perform(post("/activecard/{matchId}", testMatchId))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void wrongMethodForPlayerHand() throws Exception {
        mockMvc.perform(delete("/playerhand/{matchId}", testMatchId))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void newMatchReturnsValidResponse() throws Exception {
        UUID expectedId = UUID.randomUUID();
        when(unoService.newMatch(any(List.class))).thenReturn(expectedId);

        mockMvc.perform(post("/newmatch")
                .param("players", "TestPlayer"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void playerHandWithEmptyResponse() throws Exception {
        when(unoService.playerHand(testMatchId)).thenReturn(List.of());

        mockMvc.perform(get("/playerhand/{matchId}", testMatchId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void drawCardHandlesException() throws Exception {
        doThrow(new RuntimeException("Game error")).when(unoService)
                .drawCard(any(UUID.class), anyString());

        mockMvc.perform(post("/draw/{matchId}/{player}", testMatchId, testPlayer))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Game error"));
    }

    @Test
    public void playWithWildCard() throws Exception {
        JsonCard wildCard = new JsonCard("Red", null, "WildCard", true);

        mockMvc.perform(post("/play/{matchId}/{player}", testMatchId, testPlayer)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wildCard)))
                .andExpect(status().isOk());
    }
} 