package com.divami.java_project.service;

import com.divami.java_project.model.dto.SeasonTeamDTO;
import java.util.List;
import java.util.UUID;

/** Registers and removes teams from a season's participant list. */
public interface SeasonTeamService {

    /** Adds a team to the season. Throws DuplicateResourceException if already registered. */
    SeasonTeamDTO addTeamToSeason(UUID seasonId, UUID teamId);

    List<SeasonTeamDTO> findBySeason(UUID seasonId);

    /** Soft-removes a team from the season. Throws BusinessException if matches already exist for the team. */
    void removeFromSeason(UUID seasonTeamId);
}
