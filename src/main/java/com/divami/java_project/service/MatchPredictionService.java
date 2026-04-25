package com.divami.java_project.service;

import com.divami.java_project.model.dto.MatchPredictionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

/** Handles per-match predictions submitted by users. */
public interface MatchPredictionService {

    /**
     * Submits a prediction for a match.
     * Throws PredictionLockedException if current time is past the match's prediction_lock_time.
     * Throws DuplicateResourceException if the user already has an active prediction for this match.
     */
    MatchPredictionDTO submit(UUID matchId, UUID userId, MatchPredictionDTO dto);

    /**
     * Updates an existing prediction.
     * Throws PredictionLockedException if the window is closed.
     */
    MatchPredictionDTO update(UUID predictionId, UUID userId, MatchPredictionDTO dto);

    /** Returns the calling user's own prediction for a match. */
    MatchPredictionDTO findMyPrediction(UUID matchId, UUID userId);

    /**
     * Returns all predictions for a match — accessible only after the prediction window has closed.
     * Throws BusinessException if window is still open and caller is not admin.
     */
    Page<MatchPredictionDTO> findAllByMatch(UUID matchId, Pageable pageable);
}
