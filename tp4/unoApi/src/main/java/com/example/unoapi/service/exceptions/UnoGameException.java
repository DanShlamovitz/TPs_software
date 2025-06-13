package com.example.unoapi.service.exceptions;

public abstract class UnoGameException extends RuntimeException {
    public UnoGameException(String message) {
        super(message);
    }
    
    public UnoGameException(String message, Throwable cause) {
        super(message, cause);
    }
} 