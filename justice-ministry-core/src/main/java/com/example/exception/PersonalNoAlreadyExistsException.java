package com.example.exception;

public class PersonalNoAlreadyExistsException extends RuntimeException {
    public PersonalNoAlreadyExistsException(String message) {
        super(message);
    }
}
