package com.example.unoapi.controller;

import com.example.unoapi.model.*;
import com.example.unoapi.service.UnoService;
import com.example.unoapi.service.Dealer;
import com.example.unoapi.service.exceptions.*;
import com.fasterxml.jackson.core.type.TypeReference;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UnoController.class)
class UnoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UnoService unoService;

    @MockBean
    private Dealer dealer;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID testMatchId;
    private List<Card> testHand;

    @BeforeEach
    void setUp() {
        when(dealer.fullDeck()).thenReturn(UnoService.fullDeck());
        
        testMatchId = UUID.randomUUID();
        testHand = Arrays.asList(
            new NumberCard("red", 1),
            new NumberCard("blue", 2),
            new SkipCard("green")
        );
    }

    @Test
    void playWrongTurnTest() throws Exception {
        String uuid = newGame(); // crear un juego con 2 jugadores

        List<JsonCard> hand = playerHand(uuid);

        // Mock the service to throw the not player turn exception
        doThrow(new RuntimeException(Player.NotPlayersTurn + "B"))
                .when(unoService).play(any(UUID.class), eq("B"), any(JsonCard.class));

        String resp = mockMvc.perform(post("/play/" + uuid + "/B")
                .contentType(MediaType.APPLICATION_JSON)
                .content(hand.get(0).toString()))
                .andDo(print())
                .andExpect(status().is(500))
                .andReturn().getResponse().getContentAsString();

        assertTrue(resp.contains(Player.NotPlayersTurn + "B"));
    }

    private String newGame() throws Exception {
        when(unoService.newMatch(anyList())).thenReturn(testMatchId);
        
        String resp = mockMvc.perform(post("/newmatch?players=A&players=B"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        return new ObjectMapper().readTree(resp).asText();
    }

    private List<JsonCard> playerHand(String uuid) throws Exception {
        // Mock the service to return our test hand
        when(unoService.playerHand(any(UUID.class))).thenReturn(testHand);
        String resp = mockMvc.perform(get("/playerhand/" + uuid))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        return new ObjectMapper().readValue(resp, new TypeReference<List<JsonCard>>() {});
    }
} 