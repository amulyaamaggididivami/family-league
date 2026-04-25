package com.divami.java_project.repository;

import com.divami.java_project.model.MatchPrediction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MatchPredictionRepository extends JpaRepository<MatchPrediction, UUID> {

    Optional<MatchPrediction> findByMatchIdAndUserIdAndDeletedAtIsNull(UUID matchId, UUID userId);

    Optional<MatchPrediction> findByIdAndDeletedAtIsNull(UUID id);

    /** Paginated — used for head-to-head visibility after prediction window closes. */
    Page<MatchPrediction> findByMatchIdAndDeletedAtIsNull(UUID matchId, Pageable pageable);

    /** Full list — used by the scoring engine after result publication. */
    List<MatchPrediction> findByMatchIdAndDeletedAtIsNull(UUID matchId);

    boolean existsByMatchIdAndUserIdAndDeletedAtIsNull(UUID matchId, UUID userId);

    /** Returns user IDs who have already submitted a prediction for this match. */
    @Query("SELECT mp.user.id FROM MatchPrediction mp WHERE mp.match.id = :matchId AND mp.deletedAt IS NULL")
    List<UUID> findUserIdsWithPredictionForMatch(@Param("matchId") UUID matchId);
}
