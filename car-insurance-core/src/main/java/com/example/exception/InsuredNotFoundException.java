package com.example.exception;

public class InsuredNotFoundException extends RuntimeException {
    public InsuredNotFoundException(String message) {
        super(message);
    }
}
