package com.divami.java_project.service;

import com.divami.java_project.model.dto.LeaguePredictionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

/** Handles league-level team ranking predictions submitted by users. */
public interface LeaguePredictionService {

    /**
     * Submits a full team ranking prediction for a season.
     * Throws PredictionLockedException if past the season's league_prediction_lock_time.
     * Throws DuplicateResourceException if the user already has an active prediction for this season.
     */
    LeaguePredictionDTO submit(UUID seasonId, UUID userId, LeaguePredictionDTO dto);

    /**
     * Updates an existing league prediction (replaces all team rankings).
     * Throws PredictionLockedException if the window is closed.
     */
    LeaguePredictionDTO update(UUID predictionId, UUID userId, LeaguePredictionDTO dto);

    /** Returns the calling user's own league prediction for a season. */
    LeaguePredictionDTO findMyPrediction(UUID seasonId, UUID userId);

    /**
     * Returns all league predictions for a season.
     * Visible only after the league prediction lock time has passed.
     */
    Page<LeaguePredictionDTO> findAllBySeason(UUID seasonId, Pageable pageable);
}
