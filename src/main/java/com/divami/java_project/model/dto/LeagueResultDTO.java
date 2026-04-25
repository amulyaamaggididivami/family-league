package com.divami.java_project.model.dto;

import java.time.Instant;
import java.util.UUID;

/** Transfer object for {@link com.divami.java_project.model.LeagueResult}. */
public record LeagueResultDTO(
        UUID id,
        UUID seasonId,
        UUID firstPlaceTeamId,
        String firstPlaceTeamName,
        UUID secondPlaceTeamId,
        String secondPlaceTeamName,
        UUID thirdPlaceTeamId,
        String thirdPlaceTeamName,
        Instant publishedAt,
        Instant verifiedAt
) {}
