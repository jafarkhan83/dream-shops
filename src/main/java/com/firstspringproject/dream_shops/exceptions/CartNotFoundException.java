package com.firstspringproject.dream_shops.exceptions;

public class CartNotFoundException extends RuntimeException {
    
    public CartNotFoundException(String message) {
        super(message);
    }
}
