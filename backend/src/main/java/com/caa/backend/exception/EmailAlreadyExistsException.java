package com.caa.backend.exception;

/**
 * Thrown when a registration attempt uses an email that is already registered.
 * Maps to HTTP 409 Conflict.
 *
 * NOTE: Register this in your existing GlobalExceptionHandler with @ExceptionHandler.
 *       Add: @ExceptionHandler(EmailAlreadyExistsException.class)
 *            public ResponseEntity<ErrorResponse> handleEmailExists(EmailAlreadyExistsException ex)
 *            → return ResponseEntity.status(HttpStatus.CONFLICT).body(...)
 */
public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(String email) {
        super("Email is already registered: " + email);
    }
}

