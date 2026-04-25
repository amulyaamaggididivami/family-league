package com.divami.java_project.model.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Transfer object for {@link com.divami.java_project.model.LeaguePrediction}.
 * Includes the ordered team rankings for convenience on read; on write, teamRankings must be provided.
 */
public record LeaguePredictionDTO(
        UUID id,
        @NotNull UUID seasonId,
        UUID userId,
        Instant submittedAt,
        List<LeagueTeamPredictionDTO> teamRankings
) {}
