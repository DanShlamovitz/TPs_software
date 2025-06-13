package com.example.unoapi.service.exceptions;

public class CardNotInHandException extends UnoGameException {
    public CardNotInHandException(String playerName) {
        super("Not a card in hand of " + playerName);
    }
} 