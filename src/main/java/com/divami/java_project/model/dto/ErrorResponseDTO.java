package com.divami.java_project.model.dto;

import java.time.Instant;

/** Uniform error payload returned by {@link com.divami.java_project.exception.GlobalExceptionHandler} for all error responses. */
public record ErrorResponseDTO(String code, String message, Instant timestamp) {}
