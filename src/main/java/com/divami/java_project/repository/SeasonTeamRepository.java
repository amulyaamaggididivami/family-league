package com.divami.java_project.repository;

import com.divami.java_project.model.SeasonTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SeasonTeamRepository extends JpaRepository<SeasonTeam, UUID> {

    List<SeasonTeam> findBySeasonIdAndDeletedAtIsNull(UUID seasonId);

    Optional<SeasonTeam> findBySeasonIdAndTeamIdAndDeletedAtIsNull(UUID seasonId, UUID teamId);

    Optional<SeasonTeam> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsBySeasonIdAndTeamIdAndDeletedAtIsNull(UUID seasonId, UUID teamId);
}
