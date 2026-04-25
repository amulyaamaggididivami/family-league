package com.divami.java_project.model.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

/** Transfer object for {@link com.divami.java_project.model.Team}. */
public record TeamDTO(
        UUID id,
        @NotBlank String name,
        @NotBlank String shortCode,
        String logoUrl
) {}
