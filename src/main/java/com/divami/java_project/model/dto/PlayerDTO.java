package com.divami.java_project.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/** Transfer object for {@link com.divami.java_project.model.Player}. */
public record PlayerDTO(
        UUID id,
        @NotNull UUID teamId,
        String teamName,
        @NotBlank String name,
        String position,
        boolean isActive
) {}
