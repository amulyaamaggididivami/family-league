package com.divami.java_project.repository;

import com.divami.java_project.model.LeaderboardSnapshot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeaderboardSnapshotRepository extends JpaRepository<LeaderboardSnapshot, UUID> {

    Page<LeaderboardSnapshot> findBySeasonId(UUID seasonId, Pageable pageable);

    /**
     * Returns the single most recent snapshot timestamp for a season.
     * Used to identify which snapshot rows represent the "current" leaderboard.
     */
    @Query("SELECT MAX(s.snapshotAt) FROM LeaderboardSnapshot s WHERE s.season.id = :seasonId")
    Optional<Instant> findLatestSnapshotAt(@Param("seasonId") UUID seasonId);

    List<LeaderboardSnapshot> findBySeasonIdAndSnapshotAtOrderByRankAsc(UUID seasonId, Instant snapshotAt);
}
