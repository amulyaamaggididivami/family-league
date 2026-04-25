package com.divami.java_project.repository;

import com.divami.java_project.model.LeagueResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeagueResultRepository extends JpaRepository<LeagueResult, UUID> {

    Optional<LeagueResult> findBySeasonIdAndDeletedAtIsNull(UUID seasonId);

    boolean existsBySeasonIdAndDeletedAtIsNull(UUID seasonId);
}
