package com.divami.java_project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

/**
 * Immutable ledger entry recording points earned for a single correct prediction.
 * Records are only ever inserted — never updated or deleted — by the server-side scoring engine.
 * Exactly one of match_prediction_id or league_prediction_id is non-null per row.
 */
@Entity
@Table(name = "prediction_points")
public class PredictionPoints {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private LeagueSeason season;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    /** Set when this point was earned from a match-level prediction. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_prediction_id")
    private MatchPrediction matchPrediction;

    /** Set when this point was earned from a league-level prediction. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_prediction_id")
    private LeaguePrediction leaguePrediction;

    @Column(name = "points_earned", nullable = false)
    private Integer pointsEarned;

    /** MATCH or LEAGUE — identifies which prediction type this row scores. */
    @Column(name = "point_type", nullable = false)
    private String pointType;

    /** Which field matched: WINNER, TOSS_WINNER, PLAYER_OF_MATCH, LEAGUE_RANK. */
    @Column(name = "source_field", nullable = false)
    private String sourceField;

    @Column(name = "calculated_at", nullable = false)
    private Instant calculatedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LeagueSeason getSeason() { return season; }
    public void setSeason(LeagueSeason season) { this.season = season; }

    public Match getMatch() { return match; }
    public void setMatch(Match match) { this.match = match; }

    public MatchPrediction getMatchPrediction() { return matchPrediction; }
    public void setMatchPrediction(MatchPrediction matchPrediction) { this.matchPrediction = matchPrediction; }

    public LeaguePrediction getLeaguePrediction() { return leaguePrediction; }
    public void setLeaguePrediction(LeaguePrediction leaguePrediction) { this.leaguePrediction = leaguePrediction; }

    public Integer getPointsEarned() { return pointsEarned; }
    public void setPointsEarned(Integer pointsEarned) { this.pointsEarned = pointsEarned; }

    public String getPointType() { return pointType; }
    public void setPointType(String pointType) { this.pointType = pointType; }

    public String getSourceField() { return sourceField; }
    public void setSourceField(String sourceField) { this.sourceField = sourceField; }

    public Instant getCalculatedAt() { return calculatedAt; }
    public void setCalculatedAt(Instant calculatedAt) { this.calculatedAt = calculatedAt; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
