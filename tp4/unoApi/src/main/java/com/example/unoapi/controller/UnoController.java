package com.example.unoapi.controller;

import com.example.unoapi.service.UnoService;
import com.example.unoapi.model.JsonCard;
import com.example.unoapi.service.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class UnoController {
    @Autowired
    UnoService unoService;

    @PostMapping("newmatch")
    public ResponseEntity newMatch(@RequestParam List<String> players) {
        UUID matchId = unoService.newMatch(players);
        return ResponseEntity.ok(matchId);
    }

    @PostMapping("play/{matchId}/{player}")
    public ResponseEntity play(@PathVariable UUID matchId, @PathVariable String player, @RequestBody JsonCard card) {
        unoService.play(matchId, player, card);
        return ResponseEntity.ok().build();
    }

    @PostMapping("draw/{matchId}/{player}")
    public ResponseEntity drawCard(@PathVariable UUID matchId, @PathVariable String player) {
        unoService.drawCard(matchId, player);
        return ResponseEntity.ok().build();
    }

    @GetMapping("activecard/{matchId}")
    public ResponseEntity activeCard(@PathVariable UUID matchId) {
        return ResponseEntity.ok(unoService.activeCard(matchId).asJson());
    }

    @GetMapping("playerhand/{matchId}")
    public ResponseEntity playerHand(@PathVariable UUID matchId) {
        return ResponseEntity.ok(unoService.playerHand(matchId).stream().map(card -> card.asJson()).toList());
    }

    // Exception Handlers
    @ExceptionHandler(MatchNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleMatchNotFoundException(MatchNotFoundException ex, WebRequest request) {
        Map<String, Object> body = createErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Match Not Found",
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotPlayerTurnException.class)
    public ResponseEntity<Map<String, Object>> handleNotPlayerTurnException(NotPlayerTurnException ex, WebRequest request) {
        Map<String, Object> body = createErrorResponse(
            HttpStatus.FORBIDDEN.value(),
            "Not Player's Turn",
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CardNotInHandException.class)
    public ResponseEntity<Map<String, Object>> handleCardNotInHandException(CardNotInHandException ex, WebRequest request) {
        Map<String, Object> body = createErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Card Not in Hand",
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCardPlayException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCardPlayException(InvalidCardPlayException ex, WebRequest request) {
        Map<String, Object> body = createErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid Card Play",
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GameOverException.class)
    public ResponseEntity<Map<String, Object>> handleGameOverException(GameOverException ex, WebRequest request) {
        Map<String, Object> body = createErrorResponse(
            HttpStatus.CONFLICT.value(),
            "Game Over",
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCardException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCardException(InvalidCardException ex, WebRequest request) {
        Map<String, Object> body = createErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid Card",
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnoGameException.class)
    public ResponseEntity<Map<String, Object>> handleUnoGameException(UnoGameException ex, WebRequest request) {
        Map<String, Object> body = createErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Game Error",
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex, WebRequest request) {
        Map<String, Object> body = createErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "An unexpected error occurred: " + ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, Object> createErrorResponse(int status, String error, String message, String path) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status);
        body.put("error", error);
        body.put("message", message);
        body.put("path", path.replace("uri=", ""));
        return body;
    }
}