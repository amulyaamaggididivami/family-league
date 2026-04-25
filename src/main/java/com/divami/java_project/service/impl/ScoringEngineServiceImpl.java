package com.divami.java_project.service.impl;

import com.divami.java_project.model.LeaderboardSnapshot;
import com.divami.java_project.model.LeaguePrediction;
import com.divami.java_project.model.LeagueResult;
import com.divami.java_project.model.LeagueSeason;
import com.divami.java_project.model.LeagueSeasonMember;
import com.divami.java_project.model.LeagueTeamPrediction;
import com.divami.java_project.model.Match;
import com.divami.java_project.model.MatchPrediction;
import com.divami.java_project.model.MatchResult;
import com.divami.java_project.model.PredictionPoints;
import com.divami.java_project.model.User;
import com.divami.java_project.repository.LeaderboardSnapshotRepository;
import com.divami.java_project.repository.LeaguePredictionRepository;
import com.divami.java_project.repository.LeagueResultRepository;
import com.divami.java_project.repository.LeagueSeasonMemberRepository;
import com.divami.java_project.repository.LeagueTeamPredictionRepository;
import com.divami.java_project.repository.MatchPredictionRepository;
import com.divami.java_project.repository.MatchRepository;
import com.divami.java_project.repository.MatchResultRepository;
import com.divami.java_project.repository.PredictionPointsRepository;
import com.divami.java_project.service.ScoringEngineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Scoring rules (§6 of FamilyLeague.md):
 *   - Each correct prediction field = 1 point.
 *   - Match fields: WINNER, TOSS_WINNER, PLAYER_OF_MATCH.
 *   - Ties count as a correct winner prediction for both sides.
 *   - League fields: exact 1st/2nd/3rd place prediction = 1 point each (LEAGUE_RANK).
 * Runs asynchronously on the {@code taskExecutor} thread pool.
 */
@Service
public class ScoringEngineServiceImpl implements ScoringEngineService {

    private static final Logger log = LoggerFactory.getLogger(ScoringEngineServiceImpl.class);

    private final MatchRepository matchRepository;
    private final MatchResultRepository matchResultRepository;
    private final MatchPredictionRepository matchPredictionRepository;
    private final LeaguePredictionRepository leaguePredictionRepository;
    private final LeagueTeamPredictionRepository leagueTeamPredictionRepository;
    private final LeagueResultRepository leagueResultRepository;
    private final LeagueSeasonMemberRepository memberRepository;
    private final PredictionPointsRepository pointsRepository;
    private final LeaderboardSnapshotRepository snapshotRepository;

    public ScoringEngineServiceImpl(MatchRepository matchRepository,
                                     MatchResultRepository matchResultRepository,
                                     MatchPredictionRepository matchPredictionRepository,
                                     LeaguePredictionRepository leaguePredictionRepository,
                                     LeagueTeamPredictionRepository leagueTeamPredictionRepository,
                                     LeagueResultRepository leagueResultRepository,
                                     LeagueSeasonMemberRepository memberRepository,
                                     PredictionPointsRepository pointsRepository,
                                     LeaderboardSnapshotRepository snapshotRepository) {
        this.matchRepository = matchRepository;
        this.matchResultRepository = matchResultRepository;
        this.matchPredictionRepository = matchPredictionRepository;
        this.leaguePredictionRepository = leaguePredictionRepository;
        this.leagueTeamPredictionRepository = leagueTeamPredictionRepository;
        this.leagueResultRepository = leagueResultRepository;
        this.memberRepository = memberRepository;
        this.pointsRepository = pointsRepository;
        this.snapshotRepository = snapshotRepository;
    }

    @Override
    @Async("taskExecutor")
    @Transactional
    public void calculateMatchPoints(UUID matchId) {
        log.info("Scoring: starting match points calculation for matchId={}", matchId);

        Match match = matchRepository.findByIdAndDeletedAtIsNull(matchId).orElse(null);
        if (match == null) {
            log.warn("Scoring: matchId={} not found, aborting", matchId);
            return;
        }
        MatchResult result = matchResultRepository.findByMatchIdAndDeletedAtIsNull(matchId).orElse(null);
        if (result == null || result.getPublishedAt() == null) {
            log.warn("Scoring: no published result for matchId={}, aborting", matchId);
            return;
        }

        List<MatchPrediction> predictions = matchPredictionRepository.findByMatchIdAndDeletedAtIsNull(matchId);
        List<PredictionPoints> earned = new ArrayList<>();

        for (MatchPrediction p : predictions) {
            // Winner (tie means both sides earn the point)
            if (result.isTie() ||
                    (result.getWinningTeam() != null && p.getPredictedWinner() != null
                            && result.getWinningTeam().getId().equals(p.getPredictedWinner().getId()))) {
                earned.add(buildPoint(p.getUser(), match.getSeason(), match,
                        p, null, 1, "MATCH", "WINNER", result));
            }
            // Toss winner
            if (result.getTossWinnerTeam() != null && p.getPredictedTossWinner() != null
                    && result.getTossWinnerTeam().getId().equals(p.getPredictedTossWinner().getId())) {
                earned.add(buildPoint(p.getUser(), match.getSeason(), match,
                        p, null, 1, "MATCH", "TOSS_WINNER", result));
            }
            // Player of the match
            if (result.getPlayerOfMatch() != null && p.getPredictedPlayerOfMatch() != null
                    && result.getPlayerOfMatch().getId().equals(p.getPredictedPlayerOfMatch().getId())) {
                earned.add(buildPoint(p.getUser(), match.getSeason(), match,
                        p, null, 1, "MATCH", "PLAYER_OF_MATCH", result));
            }
        }

        pointsRepository.saveAll(earned);
        log.info("Scoring: saved {} point records for matchId={}", earned.size(), matchId);

        recalculateLeaderboard(match.getSeason().getId());
    }

    @Override
    @Async("taskExecutor")
    @Transactional
    public void calculateLeaguePoints(UUID seasonId) {
        log.info("Scoring: starting league points calculation for seasonId={}", seasonId);

        LeagueResult result = leagueResultRepository.findBySeasonIdAndDeletedAtIsNull(seasonId).orElse(null);
        if (result == null || result.getPublishedAt() == null) {
            log.warn("Scoring: no published league result for seasonId={}, aborting", seasonId);
            return;
        }

        List<LeaguePrediction> predictions = leaguePredictionRepository.findBySeasonIdAndDeletedAtIsNull(seasonId);
        List<PredictionPoints> earned = new ArrayList<>();

        for (LeaguePrediction p : predictions) {
            List<LeagueTeamPrediction> rankings =
                    leagueTeamPredictionRepository.findByLeaguePredictionIdAndDeletedAtIsNull(p.getId());

            for (LeagueTeamPrediction rank : rankings) {
                UUID teamId = rank.getTeam().getId();
                boolean correct = false;

                if (rank.getPredictedRank() == 1 && result.getFirstPlaceTeam() != null
                        && result.getFirstPlaceTeam().getId().equals(teamId)) correct = true;
                else if (rank.getPredictedRank() == 2 && result.getSecondPlaceTeam() != null
                        && result.getSecondPlaceTeam().getId().equals(teamId)) correct = true;
                else if (rank.getPredictedRank() == 3 && result.getThirdPlaceTeam() != null
                        && result.getThirdPlaceTeam().getId().equals(teamId)) correct = true;

                if (correct) {
                    earned.add(buildPoint(p.getUser(), result.getSeason(), null,
                            null, p, 1, "LEAGUE", "LEAGUE_RANK", null));
                }
            }
        }

        pointsRepository.saveAll(earned);
        log.info("Scoring: saved {} league point records for seasonId={}", earned.size(), seasonId);

        recalculateLeaderboard(seasonId);
    }

    // Called inside the @Async methods above — runs on the same thread, no proxy needed.
    private void recalculateLeaderboard(UUID seasonId) {
        log.info("Leaderboard: recalculating for seasonId={}", seasonId);

        List<LeagueSeasonMember> members = memberRepository.findBySeasonIdAndDeletedAtIsNull(seasonId);
        if (members.isEmpty()) return;

        // Total points per user
        Map<UUID, Integer> totals = members.stream().collect(
                Collectors.toMap(m -> m.getUser().getId(),
                        m -> pointsRepository.sumPointsByUserAndSeason(m.getUser().getId(), seasonId)));

        // Sort descending by total, assign rank (ties share the same rank)
        List<Map.Entry<UUID, Integer>> sorted = totals.entrySet().stream()
                .sorted(Map.Entry.<UUID, Integer>comparingByValue(Comparator.reverseOrder()))
                .toList();

        Instant snapshotAt = Instant.now();
        List<LeaderboardSnapshot> snapshots = new ArrayList<>();
        AtomicInteger rank = new AtomicInteger(1);
        int prevPoints = -1;
        int prevRank = 1;

        Map<UUID, User> userMap = members.stream()
                .collect(Collectors.toMap(m -> m.getUser().getId(), LeagueSeasonMember::getUser));
        LeagueSeason season = members.get(0).getSeason();

        for (int i = 0; i < sorted.size(); i++) {
            UUID userId = sorted.get(i).getKey();
            int points = sorted.get(i).getValue();
            int assignedRank = (points == prevPoints) ? prevRank : rank.get();

            LeaderboardSnapshot snap = new LeaderboardSnapshot();
            snap.setSeason(season);
            snap.setUser(userMap.get(userId));
            snap.setTotalPoints(points);
            snap.setRank(assignedRank);
            snap.setSnapshotAt(snapshotAt);
            snap.setCreatedAt(snapshotAt);
            snapshots.add(snap);

            prevPoints = points;
            prevRank = assignedRank;
            rank.set(i + 2);
        }

        snapshotRepository.saveAll(snapshots);
        log.info("Leaderboard: snapshot saved for seasonId={} ({} entries)", seasonId, snapshots.size());
    }

    private PredictionPoints buildPoint(User user, LeagueSeason season, Match match,
                                        MatchPrediction matchPred, LeaguePrediction leaguePred,
                                        int points, String pointType, String sourceField,
                                        MatchResult result) {
        PredictionPoints p = new PredictionPoints();
        p.setUser(user);
        p.setSeason(season);
        p.setMatch(match);
        p.setMatchPrediction(matchPred);
        p.setLeaguePrediction(leaguePred);
        p.setPointsEarned(points);
        p.setPointType(pointType);
        p.setSourceField(sourceField);
        p.setCalculatedAt(Instant.now());
        p.setCreatedAt(Instant.now());
        return p;
    }
}
