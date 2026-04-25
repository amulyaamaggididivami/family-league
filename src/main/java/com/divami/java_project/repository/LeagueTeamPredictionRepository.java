package com.divami.java_project.repository;

import com.divami.java_project.model.LeagueTeamPrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface LeagueTeamPredictionRepository extends JpaRepository<LeagueTeamPrediction, UUID> {

    List<LeagueTeamPrediction> findByLeaguePredictionIdAndDeletedAtIsNull(UUID leaguePredictionId);

    void deleteByLeaguePredictionId(UUID leaguePredictionId);
}
