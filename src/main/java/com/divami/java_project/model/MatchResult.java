package com.divami.java_project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

/**
 * The admin-entered and admin-verified result for a completed {@link Match}.
 * Points calculation is triggered after the result is published (publishedAt is set).
 * Verification step (verifiedAt) is separate from publication to support a two-step admin workflow.
 */
@Entity
@Table(name = "match_results")
public class MatchResult {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false, unique = true)
    private Match match;

    /** Null when is_tie = true. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winning_team_id")
    private Team winningTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toss_winner_team_id")
    private Team tossWinnerTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_of_match_id")
    private Player playerOfMatch;

    /** When true, both teams receive points for a correct match-winner prediction. */
    @Column(name = "is_tie", nullable = false)
    private boolean isTie = false;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "published_by")
    private UUID publishedBy;

    /** Admin verifies accuracy of entered result before triggering point calculation. */
    @Column(name = "verified_at")
    private Instant verifiedAt;

    @Column(name = "verified_by")
    private UUID verifiedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "created_by", updatable = false)
    private UUID createdBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "updated_by")
    private UUID updatedBy;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Match getMatch() { return match; }
    public void setMatch(Match match) { this.match = match; }

    public Team getWinningTeam() { return winningTeam; }
    public void setWinningTeam(Team winningTeam) { this.winningTeam = winningTeam; }

    public Team getTossWinnerTeam() { return tossWinnerTeam; }
    public void setTossWinnerTeam(Team tossWinnerTeam) { this.tossWinnerTeam = tossWinnerTeam; }

    public Player getPlayerOfMatch() { return playerOfMatch; }
    public void setPlayerOfMatch(Player playerOfMatch) { this.playerOfMatch = playerOfMatch; }

    public boolean isTie() { return isTie; }
    public void setTie(boolean tie) { isTie = tie; }

    public Instant getPublishedAt() { return publishedAt; }
    public void setPublishedAt(Instant publishedAt) { this.publishedAt = publishedAt; }

    public UUID getPublishedBy() { return publishedBy; }
    public void setPublishedBy(UUID publishedBy) { this.publishedBy = publishedBy; }

    public Instant getVerifiedAt() { return verifiedAt; }
    public void setVerifiedAt(Instant verifiedAt) { this.verifiedAt = verifiedAt; }

    public UUID getVerifiedBy() { return verifiedBy; }
    public void setVerifiedBy(UUID verifiedBy) { this.verifiedBy = verifiedBy; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public UUID getCreatedBy() { return createdBy; }
    public void setCreatedBy(UUID createdBy) { this.createdBy = createdBy; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public UUID getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(UUID updatedBy) { this.updatedBy = updatedBy; }

    public Instant getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Instant deletedAt) { this.deletedAt = deletedAt; }
}
