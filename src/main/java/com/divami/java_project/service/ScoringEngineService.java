package com.divami.java_project.service;

import java.util.UUID;

/**
 * Async scoring engine invoked after a result is published.
 * Each method runs on the {@code taskExecutor} thread pool so that the publish
 * HTTP response is returned immediately while calculation happens in the background.
 */
public interface ScoringEngineService {

    /** Calculates match-level prediction points and refreshes the season leaderboard. */
    void calculateMatchPoints(UUID matchId);

    /** Calculates league-level prediction points and produces the final leaderboard snapshot. */
    void calculateLeaguePoints(UUID seasonId);
}
