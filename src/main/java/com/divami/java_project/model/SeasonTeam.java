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
import java.util.UUID;

/**
 * Registers a {@link Team} to participate in a specific {@link LeagueSeason}.
 * Allows the same team to appear in multiple seasons without duplicating the team record.
 */
@Entity
@Table(name = "season_teams")
public class SeasonTeam extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private LeagueSeason season;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public LeagueSeason getSeason() { return season; }
    public void setSeason(LeagueSeason season) { this.season = season; }

    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }
}
