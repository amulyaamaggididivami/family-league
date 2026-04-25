package com.divami.java_project.repository;

import com.divami.java_project.model.Match;
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
public interface MatchRepository extends JpaRepository<Match, UUID> {

    Page<Match> findBySeasonIdAndDeletedAtIsNull(UUID seasonId, Pageable pageable);

    Page<Match> findBySeasonIdAndStatusAndDeletedAtIsNull(UUID seasonId, String status, Pageable pageable);

    Optional<Match> findByIdAndDeletedAtIsNull(UUID id);

    /** Used by the notification scheduler to find matches starting soon with open prediction windows. */
    @Query("SELECT m FROM Match m WHERE m.scheduledAt BETWEEN :from AND :to " +
           "AND m.deletedAt IS NULL AND m.status = 'SCHEDULED'")
    List<Match> findScheduledBetween(@Param("from") Instant from, @Param("to") Instant to);
}
