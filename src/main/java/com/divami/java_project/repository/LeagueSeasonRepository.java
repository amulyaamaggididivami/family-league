package com.divami.java_project.repository;

import com.divami.java_project.model.LeagueSeason;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeagueSeasonRepository extends JpaRepository<LeagueSeason, UUID> {

    Page<LeagueSeason> findByLeagueIdAndDeletedAtIsNull(UUID leagueId, Pageable pageable);

    Optional<LeagueSeason> findByIdAndDeletedAtIsNull(UUID id);

    /** Returns the earliest scheduled match time for a season — used to compute league_prediction_lock_time. */
    @Query("SELECT MIN(m.scheduledAt) FROM Match m WHERE m.season.id = :seasonId AND m.deletedAt IS NULL")
    Optional<java.time.Instant> findFirstMatchScheduledAt(@Param("seasonId") UUID seasonId);
}
