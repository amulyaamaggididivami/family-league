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
 * Records that a {@link User} is participating in a specific {@link LeagueSeason}.
 * Only members can submit predictions for that season.
 */
@Entity
@Table(name = "league_season_members")
public class LeagueSeasonMember extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private LeagueSeason season;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** ACTIVE, INACTIVE, REMOVED. */
    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "joined_at")
    private Instant joinedAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public LeagueSeason getSeason() { return season; }
    public void setSeason(LeagueSeason season) { this.season = season; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Instant getJoinedAt() { return joinedAt; }
    public void setJoinedAt(Instant joinedAt) { this.joinedAt = joinedAt; }
}
