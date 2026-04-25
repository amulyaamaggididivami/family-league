package com.divami.java_project.model.dto;

import java.time.Instant;
import java.util.UUID;

/** Transfer object for {@link com.divami.java_project.model.PredictionPoints}. Read-only — points are server-calculated. */
public record PredictionPointsDTO(
        UUID id,
        UUID userId,
        UUID seasonId,
        UUID matchId,
        UUID matchPredictionId,
        UUID leaguePredictionId,
        Integer pointsEarned,
        String pointType,
        String sourceField,
        Instant calculatedAt
) {}
