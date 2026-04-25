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
 * The admin-entered and admin-verified final standings for a {@link LeagueSeason}.
 * Publishing (publishedAt) triggers the final leaderboard calculation.
 * Verification (verifiedAt) is a separate admin confirmation step before publication.
 */
@Entity
@Table(name = "league_results")
public class LeagueResult {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", nullable = false, unique = true)
    private LeagueSeason season;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_place_team_id")
    private Team firstPlaceTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "second_place_team_id")
    private Team secondPlaceTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "third_place_team_id")
    private Team thirdPlaceTeam;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "published_by")
    private UUID publishedBy;

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

    public LeagueSeason getSeason() { return season; }
    public void setSeason(LeagueSeason season) { this.season = season; }

    public Team getFirstPlaceTeam() { return firstPlaceTeam; }
    public void setFirstPlaceTeam(Team firstPlaceTeam) { this.firstPlaceTeam = firstPlaceTeam; }

    public Team getSecondPlaceTeam() { return secondPlaceTeam; }
    public void setSecondPlaceTeam(Team secondPlaceTeam) { this.secondPlaceTeam = secondPlaceTeam; }

    public Team getThirdPlaceTeam() { return thirdPlaceTeam; }
    public void setThirdPlaceTeam(Team thirdPlaceTeam) { this.thirdPlaceTeam = thirdPlaceTeam; }

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
