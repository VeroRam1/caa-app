package com.caa.backend.exception;

/**
 * Thrown when a child profile is not found or does not belong to the requesting tutor.
 * Maps to HTTP 404 Not Found.
 *
 * NOTE: Register this in your existing GlobalExceptionHandler with @ExceptionHandler.
 *       Add: @ExceptionHandler(ChildProfileNotFoundException.class)
 *            public ResponseEntity<ErrorResponse> handleChildProfileNotFound(ChildProfileNotFoundException ex)
 *            → return ResponseEntity.status(HttpStatus.NOT_FOUND).body(...)
 */
public class ChildProfileNotFoundException extends RuntimeException {
    public ChildProfileNotFoundException(Long id) {
        super("Child profile not found with id: " + id);
    }
}
