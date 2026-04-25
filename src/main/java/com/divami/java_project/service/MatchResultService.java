package com.divami.java_project.service;

import com.divami.java_project.model.dto.MatchResultDTO;
import java.util.UUID;

/** Admin-only service for entering, verifying, and publishing match results. */
public interface MatchResultService {

    /** Creates the result record. Throws DuplicateResourceException if a result already exists for the match. */
    MatchResultDTO create(UUID matchId, MatchResultDTO dto, UUID adminId);

    /** Returns the result for a match. Throws ResourceNotFoundException if not yet entered. */
    MatchResultDTO findByMatch(UUID matchId);

    MatchResultDTO update(UUID matchId, MatchResultDTO dto, UUID adminId);

    /** Admin marks the result as verified before publication. */
    MatchResultDTO verify(UUID matchId, UUID adminId);

    /**
     * Admin publishes the result, triggering async point calculation and leaderboard update.
     * Throws BusinessException if the result has not been verified first.
     */
    MatchResultDTO publish(UUID matchId, UUID adminId);
}
