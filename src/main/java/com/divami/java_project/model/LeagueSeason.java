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
 * A phase within a {@link League} edition — e.g. Group Stage or Playoffs.
 * Controls the lifecycle and prediction lock windows for all matches within it.
 */
@Entity
@Table(name = "league_seasons")
public class LeagueSeason extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id", nullable = false)
    private League league;

    @Column(name = "season_name", nullable = false)
    private String seasonName;

    /** UPCOMING, ACTIVE, LOCKED, COMPLETED, CLOSED. */
    @Column(name = "status", nullable = false)
    private String status;

    /**
     * Deadline for league-level predictions (who finishes 1st, 2nd, … n-th).
     * Set to 4 hours before the first match's scheduled_at.
     * After this time the system rejects any league prediction submissions.
     */
    @Column(name = "league_prediction_lock_time")
    private Instant leaguePredictionLockTime;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public League getLeague() { return league; }
    public void setLeague(League league) { this.league = league; }

    public String getSeasonName() { return seasonName; }
    public void setSeasonName(String seasonName) { this.seasonName = seasonName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Instant getLeaguePredictionLockTime() { return leaguePredictionLockTime; }
    public void setLeaguePredictionLockTime(Instant leaguePredictionLockTime) { this.leaguePredictionLockTime = leaguePredictionLockTime; }

    public Instant getStartTime() { return startTime; }
    public void setStartTime(Instant startTime) { this.startTime = startTime; }

    public Instant getEndTime() { return endTime; }
    public void setEndTime(Instant endTime) { this.endTime = endTime; }
}
