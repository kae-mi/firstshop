package com.shopshop.firstshop.exception;

public class OutOfStockException extends RuntimeException {
    
    public OutOfStockException(String message) {
        super(message);
    }
} 