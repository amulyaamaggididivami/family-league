package com.divami.java_project.service;

import com.divami.java_project.model.dto.PredictionPointsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

/**
 * Read-only service for querying earned points.
 * Points are never accepted via API — they are calculated server-side after result publication.
 */
public interface PredictionPointsService {

    /** Returns points earned by a user within a season, optionally filtered to a specific match. */
    Page<PredictionPointsDTO> findByUserAndSeason(UUID userId, UUID seasonId, UUID matchId, Pageable pageable);

    /** Returns all points generated for a specific match across all users. Admin use. */
    Page<PredictionPointsDTO> findByMatch(UUID matchId, Pageable pageable);
}
