package com.divami.java_project.model.dto;

import java.time.Instant;
import java.util.UUID;

/** Transfer object for {@link com.divami.java_project.model.LeaderboardSnapshot}. Read-only — snapshots are server-generated. */
public record LeaderboardSnapshotDTO(
        UUID id,
        UUID seasonId,
        UUID userId,
        String username,
        Integer totalPoints,
        Integer rank,
        Instant snapshotAt
) {}
