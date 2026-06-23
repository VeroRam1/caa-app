package com.caa.backend.exception;

// Exception thrown when the client request is invalid. Results in HTTP 400 Bad Request response
public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
