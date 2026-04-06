package com.firstspringproject.dream_shops.exceptions;

public class CategoryAlreadyExists extends RuntimeException {

    public CategoryAlreadyExists(String message) {
        super(message);
    }
}
