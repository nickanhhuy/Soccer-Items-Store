package com.example.socceritemsstore.exception;

// Out of stock exception
public class InsufficientStockException extends RuntimeException {
    
    public InsufficientStockException(String message) {
        super(message);
    }
}
