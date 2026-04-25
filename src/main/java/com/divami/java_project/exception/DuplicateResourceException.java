package com.divami.java_project.exception;

/**
 * Thrown when a create operation would violate a unique constraint,
 * e.g. a user submitting a second prediction for the same match.
 * Maps to HTTP 409 via {@link GlobalExceptionHandler}.
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
