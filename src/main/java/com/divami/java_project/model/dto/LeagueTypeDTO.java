package com.divami.java_project.model.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

/** Transfer object for {@link com.divami.java_project.model.LeagueType}. */
public record LeagueTypeDTO(
        UUID id,
        @NotBlank String name,
        @NotBlank String shortCode,
        String country,
        String gender,
        String format,
        String governingBody,
        String description,
        boolean isActive
) {}
