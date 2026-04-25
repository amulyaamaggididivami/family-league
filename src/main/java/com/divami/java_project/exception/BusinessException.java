package com.divami.java_project.exception;

/**
 * General-purpose exception for business rule violations not covered by more
 * specific exception types (e.g. closing a league that still has open matches).
 * Maps to HTTP 400 via {@link GlobalExceptionHandler}.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
