package com.caa.backend.exception;

// Thrown when a registration attempt uses an email that is already registered. Maps to HTTP 409 Conflict.
public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(String email) {
        super("Email is already registered: " + email);
    }
}

