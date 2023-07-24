package com.example.exception;

public class VinCodeAlreadyExistsException extends RuntimeException {
    public VinCodeAlreadyExistsException(String message) {
        super(message);
    }
}
