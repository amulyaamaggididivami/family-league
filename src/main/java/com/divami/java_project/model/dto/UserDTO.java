package com.divami.java_project.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;

/** Transfer object for {@link com.divami.java_project.model.User}. Password hash is never included in responses. */
public record UserDTO(
        UUID id,
        @NotBlank String username,
        @NotBlank @Email String email,
        String fullName,
        String avatarUrl,
        boolean isActive,
        Instant createdAt
) {}
