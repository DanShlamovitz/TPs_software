package com.example.unoapi.service.exceptions;

public class NotPlayerTurnException extends UnoGameException {
    public NotPlayerTurnException(String playerName) {
        super("It is not turn of player " + playerName);
    }
} 