package com.divami.java_project.service;

import com.divami.java_project.model.dto.LeagueResultDTO;
import java.util.UUID;

/** Admin-only service for entering, verifying, and publishing league final standings. */
public interface LeagueResultService {

    /** Creates the league result record. Throws DuplicateResourceException if one already exists. */
    LeagueResultDTO create(UUID seasonId, LeagueResultDTO dto, UUID adminId);

    LeagueResultDTO findBySeason(UUID seasonId);

    LeagueResultDTO update(UUID seasonId, LeagueResultDTO dto, UUID adminId);

    /** Admin marks the result as verified before publication. */
    LeagueResultDTO verify(UUID seasonId, UUID adminId);

    /**
     * Admin publishes the final standings, which triggers the closing leaderboard calculation.
     * Throws BusinessException if not yet verified.
     */
    LeagueResultDTO publish(UUID seasonId, UUID adminId);
}
