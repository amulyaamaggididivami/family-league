package com.divami.java_project.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/** Transfer object for {@link com.divami.java_project.model.League}. */
public record LeagueDTO(
        UUID id,
        @NotNull UUID leagueTypeId,
        String leagueTypeName,
        @NotBlank String name,
        Integer seasonYear,
        String status,
        String description
) {}
