package com.divami.java_project.model.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

/** Transfer object for {@link com.divami.java_project.model.MatchPrediction}. */
public record MatchPredictionDTO(
        UUID id,
        @NotNull UUID matchId,
        UUID userId,
        UUID predictedWinnerId,
        String predictedWinnerName,
        UUID predictedTossWinnerId,
        String predictedTossWinnerName,
        UUID predictedPlayerOfMatchId,
        String predictedPlayerOfMatchName,
        Instant submittedAt
) {}
