package com.divami.java_project.model.dto;

import java.time.Instant;
import java.util.UUID;

/** Transfer object for {@link com.divami.java_project.model.MatchResult}. */
public record MatchResultDTO(
        UUID id,
        UUID matchId,
        UUID winningTeamId,
        String winningTeamName,
        UUID tossWinnerTeamId,
        String tossWinnerTeamName,
        UUID playerOfMatchId,
        String playerOfMatchName,
        boolean isTie,
        Instant publishedAt,
        Instant verifiedAt
) {}
