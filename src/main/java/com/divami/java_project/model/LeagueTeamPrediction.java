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
 * One row per team within a {@link LeaguePrediction}, recording the user's predicted finish rank.
 * A complete league prediction has exactly n rows (one per team in the season).
 */
@Entity
@Table(name = "league_team_predictions")
public class LeagueTeamPrediction extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_prediction_id", nullable = false)
    private LeaguePrediction leaguePrediction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    /** 1 = predicted winner, 2 = runner-up, etc. Must be unique within the same league_prediction_id. */
    @Column(name = "predicted_rank", nullable = false)
    private Integer predictedRank;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public LeaguePrediction getLeaguePrediction() { return leaguePrediction; }
    public void setLeaguePrediction(LeaguePrediction leaguePrediction) { this.leaguePrediction = leaguePrediction; }

    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }

    public Integer getPredictedRank() { return predictedRank; }
    public void setPredictedRank(Integer predictedRank) { this.predictedRank = predictedRank; }
}
