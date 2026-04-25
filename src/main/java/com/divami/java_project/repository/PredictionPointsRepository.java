package com.divami.java_project.repository;

import com.divami.java_project.model.PredictionPoints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface PredictionPointsRepository extends JpaRepository<PredictionPoints, UUID> {

    Page<PredictionPoints> findByUserIdAndSeasonId(UUID userId, UUID seasonId, Pageable pageable);

    Page<PredictionPoints> findByUserIdAndSeasonIdAndMatchId(UUID userId, UUID seasonId, UUID matchId, Pageable pageable);

    Page<PredictionPoints> findByMatchId(UUID matchId, Pageable pageable);

    @Query("SELECT COALESCE(SUM(p.pointsEarned), 0) FROM PredictionPoints p WHERE p.user.id = :userId AND p.season.id = :seasonId")
    Integer sumPointsByUserAndSeason(@Param("userId") UUID userId, @Param("seasonId") UUID seasonId);
}
