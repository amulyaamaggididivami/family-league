package com.divami.java_project.model.dto;

import java.util.UUID;

/** Response body after successful login or registration — carries the JWT access token. */
public record AuthResponseDTO(
        String accessToken,
        String tokenType,
        UUID userId,
        String username
) {}
