package com.example.unoapi.service.exceptions;

public class InvalidCardPlayException extends UnoGameException {
    public InvalidCardPlayException() {
        super("Card does not match Color, Number or Kind");
    }
} 