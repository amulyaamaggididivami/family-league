package com.divami.java_project.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/** Transfer object for one rank entry within a {@link LeaguePredictionDTO}. */
public record LeagueTeamPredictionDTO(
        UUID id,
        UUID leaguePredictionId,
        @NotNull UUID teamId,
        String teamName,
        @NotNull @Min(1) Integer predictedRank
) {}
