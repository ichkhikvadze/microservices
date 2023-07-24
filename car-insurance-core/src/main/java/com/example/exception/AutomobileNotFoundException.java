package com.example.exception;

public class AutomobileNotFoundException extends RuntimeException {
    public AutomobileNotFoundException(String message) {
        super(message);
    }
}
