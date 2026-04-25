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
 * A specific edition of a {@link LeagueType} — e.g. "IPL 2025".
 * Acts as an umbrella for one or more {@link LeagueSeason} instances (group stage, playoffs, etc.).
 */
@Entity
@Table(name = "leagues")
public class League extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_type_id", nullable = false)
    private LeagueType leagueType;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "season_year")
    private Integer seasonYear;

    /** UPCOMING, ACTIVE, COMPLETED, CLOSED. Closed leagues are read-only for everyone. */
    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "description")
    private String description;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public LeagueType getLeagueType() { return leagueType; }
    public void setLeagueType(LeagueType leagueType) { this.leagueType = leagueType; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getSeasonYear() { return seasonYear; }
    public void setSeasonYear(Integer seasonYear) { this.seasonYear = seasonYear; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
