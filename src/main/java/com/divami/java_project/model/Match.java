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
 * A scheduled match between two teams within a {@link LeagueSeason}.
 * The prediction_lock_time is enforced by the service layer — predictions submitted
 * after this timestamp are rejected with a {@link com.divami.java_project.exception.PredictionLockedException}.
 */
@Entity
@Table(name = "matches")
public class Match extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private LeagueSeason season;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id", nullable = false)
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_team_id", nullable = false)
    private Team awayTeam;

    @Column(name = "scheduled_at", nullable = false)
    private Instant scheduledAt;

    /** Lock time = scheduled_at minus the configured match-lock-hours (default 1 hr). */
    @Column(name = "prediction_lock_time", nullable = false)
    private Instant predictionLockTime;

    @Column(name = "venue")
    private String venue;

    /** SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED. */
    @Column(name = "status", nullable = false)
    private String status;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public LeagueSeason getSeason() { return season; }
    public void setSeason(LeagueSeason season) { this.season = season; }

    public Team getHomeTeam() { return homeTeam; }
    public void setHomeTeam(Team homeTeam) { this.homeTeam = homeTeam; }

    public Team getAwayTeam() { return awayTeam; }
    public void setAwayTeam(Team awayTeam) { this.awayTeam = awayTeam; }

    public Instant getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(Instant scheduledAt) { this.scheduledAt = scheduledAt; }

    public Instant getPredictionLockTime() { return predictionLockTime; }
    public void setPredictionLockTime(Instant predictionLockTime) { this.predictionLockTime = predictionLockTime; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
