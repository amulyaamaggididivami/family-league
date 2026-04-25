package com.divami.java_project.model.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

/** Transfer object for {@link com.divami.java_project.model.Match}. */
public record MatchDTO(
        UUID id,
        @NotNull UUID seasonId,
        @NotNull UUID homeTeamId,
        String homeTeamName,
        @NotNull UUID awayTeamId,
        String awayTeamName,
        @NotNull Instant scheduledAt,
        Instant predictionLockTime,
        String venue,
        String status
) {}
