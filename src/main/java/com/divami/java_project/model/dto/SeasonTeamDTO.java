package com.divami.java_project.model.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/** Transfer object for {@link com.divami.java_project.model.SeasonTeam}. */
public record SeasonTeamDTO(
        UUID id,
        @NotNull UUID seasonId,
        @NotNull UUID teamId,
        String teamName,
        String teamShortCode
) {}
