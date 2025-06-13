package com.example.unoapi.service.exceptions;

public class MatchNotFoundException extends UnoGameException {
    public MatchNotFoundException(String matchId) {
        super("Match not found: " + matchId);
    }
} 