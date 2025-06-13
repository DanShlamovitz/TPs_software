package com.example.unoapi.service.exceptions;

public class InvalidCardException extends UnoGameException {
    public InvalidCardException(String message) {
        super("Invalid card: " + message);
    }
    
    public InvalidCardException(String message, Throwable cause) {
        super("Invalid card: " + message, cause);
    }
} 