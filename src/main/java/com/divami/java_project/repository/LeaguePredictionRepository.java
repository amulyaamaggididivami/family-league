package com.divami.java_project.repository;

import com.divami.java_project.model.LeaguePrediction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeaguePredictionRepository extends JpaRepository<LeaguePrediction, UUID> {

    Optional<LeaguePrediction> findBySeasonIdAndUserIdAndDeletedAtIsNull(UUID seasonId, UUID userId);

    /** Paginated — used for visibility after league prediction lock time. */
    Page<LeaguePrediction> findBySeasonIdAndDeletedAtIsNull(UUID seasonId, Pageable pageable);

    /** Full list — used by the scoring engine after league result publication. */
    List<LeaguePrediction> findBySeasonIdAndDeletedAtIsNull(UUID seasonId);

    boolean existsBySeasonIdAndUserIdAndDeletedAtIsNull(UUID seasonId, UUID userId);
}
