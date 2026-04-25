package com.divami.java_project.model.dto;

import jakarta.validation.constraints.NotBlank;

/** Request body for JWT login. */
public record LoginRequestDTO(
        @NotBlank String username,
        @NotBlank String password
) {}
