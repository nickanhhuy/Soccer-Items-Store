package com.example.socceritemsstore.exception;

// Resources not found include: user, order and items.
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
