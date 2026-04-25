package com.divami.java_project.exception;

/**
 * Thrown when a user attempts to submit or modify a prediction after
 * the lock window has closed (1 hour before match start for match predictions,
 * 4 hours before first match for league predictions).
 * Maps to HTTP 422 via {@link GlobalExceptionHandler}.
 */
public class PredictionLockedException extends RuntimeException {

    public PredictionLockedException(String message) {
        super(message);
    }
}
