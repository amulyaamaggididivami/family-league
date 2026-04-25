package com.divami.java_project.model;

import com.divami.java_project.model.base.BaseAuditEntity;
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
 * A user's prediction for a single {@link Match} — covers winner, toss winner, and player of match.
 * Must be submitted before {@link Match#getPredictionLockTime()}.
 * Each user may have at most one active prediction per match; updates replace the previous values.
 */
@Entity
@Table(name = "match_predictions")
public class MatchPrediction extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "predicted_winner_id")
    private Team predictedWinner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "predicted_toss_winner_id")
    private Team predictedTossWinner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "predicted_player_of_match_id")
    private Player predictedPlayerOfMatch;

    @Column(name = "submitted_at")
    private Instant submittedAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Match getMatch() { return match; }
    public void setMatch(Match match) { this.match = match; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Team getPredictedWinner() { return predictedWinner; }
    public void setPredictedWinner(Team predictedWinner) { this.predictedWinner = predictedWinner; }

    public Team getPredictedTossWinner() { return predictedTossWinner; }
    public void setPredictedTossWinner(Team predictedTossWinner) { this.predictedTossWinner = predictedTossWinner; }

    public Player getPredictedPlayerOfMatch() { return predictedPlayerOfMatch; }
    public void setPredictedPlayerOfMatch(Player predictedPlayerOfMatch) { this.predictedPlayerOfMatch = predictedPlayerOfMatch; }

    public Instant getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(Instant submittedAt) { this.submittedAt = submittedAt; }
}
