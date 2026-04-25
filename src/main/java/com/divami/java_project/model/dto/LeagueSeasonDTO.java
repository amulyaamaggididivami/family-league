package com.divami.java_project.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

/** Transfer object for {@link com.divami.java_project.model.LeagueSeason}. */
public record LeagueSeasonDTO(
        UUID id,
        @NotNull UUID leagueId,
        String leagueName,
        @NotBlank String seasonName,
        String status,
        Instant leaguePredictionLockTime,
        Instant startTime,
        Instant endTime
) {}
