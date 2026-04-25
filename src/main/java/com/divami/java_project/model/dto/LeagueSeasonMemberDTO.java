package com.divami.java_project.model.dto;

import java.time.Instant;
import java.util.UUID;

/** Transfer object for {@link com.divami.java_project.model.LeagueSeasonMember}. */
public record LeagueSeasonMemberDTO(
        UUID id,
        UUID seasonId,
        UUID userId,
        String username,
        String status,
        Instant joinedAt
) {}
