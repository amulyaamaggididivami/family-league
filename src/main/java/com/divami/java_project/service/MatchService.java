package com.divami.java_project.service;

import com.divami.java_project.model.dto.MatchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

/** Manages match scheduling within a season. */
public interface MatchService {

    /** Supports optional status filter (SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED). */
    Page<MatchDTO> findBySeason(UUID seasonId, String status, Pageable pageable);

    MatchDTO findById(UUID id);

    /**
     * Creates a match and automatically sets prediction_lock_time to
     * scheduledAt minus the configured match-lock-hours (default: 1 hr).
     */
    MatchDTO create(UUID seasonId, MatchDTO dto);

    MatchDTO update(UUID id, MatchDTO dto);

    void delete(UUID id);
}
