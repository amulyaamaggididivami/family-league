package com.divami.java_project.service;

import com.divami.java_project.model.dto.LeaderboardSnapshotDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

/** Provides leaderboard data for a season — current standings and historical snapshots. */
public interface LeaderboardService {

    /**
     * Returns the latest leaderboard snapshot for all members of a season,
     * ordered by rank ascending.
     */
    List<LeaderboardSnapshotDTO> findCurrentBySeason(UUID seasonId);

    /** Returns the full paginated snapshot history for a season (all recalculations). */
    Page<LeaderboardSnapshotDTO> findSnapshotHistory(UUID seasonId, Pageable pageable);
}
