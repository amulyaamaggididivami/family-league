package com.divami.java_project.service.impl;

import com.divami.java_project.exception.BusinessException;
import com.divami.java_project.exception.DuplicateResourceException;
import com.divami.java_project.exception.PredictionLockedException;
import com.divami.java_project.exception.ResourceNotFoundException;
import com.divami.java_project.model.LeaguePrediction;
import com.divami.java_project.model.LeagueSeason;
import com.divami.java_project.model.LeagueTeamPrediction;
import com.divami.java_project.model.Team;
import com.divami.java_project.model.User;
import com.divami.java_project.model.dto.LeaguePredictionDTO;
import com.divami.java_project.model.dto.LeagueTeamPredictionDTO;
import com.divami.java_project.repository.LeaguePredictionRepository;
import com.divami.java_project.repository.LeagueSeasonRepository;
import com.divami.java_project.repository.LeagueTeamPredictionRepository;
import com.divami.java_project.repository.TeamRepository;
import com.divami.java_project.repository.UserRepository;
import com.divami.java_project.service.LeaguePredictionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class LeaguePredictionServiceImpl implements LeaguePredictionService {

    private static final Logger log = LoggerFactory.getLogger(LeaguePredictionServiceImpl.class);

    private final LeaguePredictionRepository predictionRepository;
    private final LeagueTeamPredictionRepository teamPredictionRepository;
    private final LeagueSeasonRepository seasonRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    public LeaguePredictionServiceImpl(LeaguePredictionRepository predictionRepository,
                                        LeagueTeamPredictionRepository teamPredictionRepository,
                                        LeagueSeasonRepository seasonRepository,
                                        UserRepository userRepository,
                                        TeamRepository teamRepository) {
        this.predictionRepository = predictionRepository;
        this.teamPredictionRepository = teamPredictionRepository;
        this.seasonRepository = seasonRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public LeaguePredictionDTO submit(UUID seasonId, UUID userId, LeaguePredictionDTO dto) {
        LeagueSeason season = seasonRepository.findByIdAndDeletedAtIsNull(seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("LeagueSeason", seasonId));
        enforceNotLocked(season);

        if (predictionRepository.existsBySeasonIdAndUserIdAndDeletedAtIsNull(seasonId, userId)) {
            throw new DuplicateResourceException("User already has a league prediction for this season");
        }
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        LeaguePrediction prediction = new LeaguePrediction();
        prediction.setSeason(season);
        prediction.setUser(user);
        prediction.setSubmittedAt(Instant.now());
        prediction.setCreatedAt(Instant.now());
        LeaguePrediction saved = predictionRepository.save(prediction);

        saveTeamRankings(saved, dto.teamRankings());

        log.info("User {} submitted league prediction for seasonId={}", userId, seasonId);
        return convertToDTO(saved);
    }

    @Override
    public LeaguePredictionDTO update(UUID predictionId, UUID userId, LeaguePredictionDTO dto) {
        LeaguePrediction prediction = predictionRepository.findById(predictionId)
                .orElseThrow(() -> new ResourceNotFoundException("LeaguePrediction", predictionId));
        enforceNotLocked(prediction.getSeason());

        // Replace all existing rankings
        teamPredictionRepository.deleteByLeaguePredictionId(predictionId);
        saveTeamRankings(prediction, dto.teamRankings());

        prediction.setSubmittedAt(Instant.now());
        prediction.setUpdatedAt(Instant.now());

        log.info("User {} updated league predictionId={}", userId, predictionId);
        return convertToDTO(predictionRepository.save(prediction));
    }

    @Override
    @Transactional(readOnly = true)
    public LeaguePredictionDTO findMyPrediction(UUID seasonId, UUID userId) {
        return convertToDTO(predictionRepository.findBySeasonIdAndUserIdAndDeletedAtIsNull(seasonId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("LeaguePrediction for user", userId)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaguePredictionDTO> findAllBySeason(UUID seasonId, Pageable pageable) {
        LeagueSeason season = seasonRepository.findByIdAndDeletedAtIsNull(seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("LeagueSeason", seasonId));
        Instant lockTime = season.getLeaguePredictionLockTime();
        if (lockTime != null && Instant.now().isBefore(lockTime)) {
            throw new BusinessException("League predictions are not visible until the prediction window closes");
        }
        return predictionRepository.findBySeasonIdAndDeletedAtIsNull(seasonId, pageable).map(this::convertToDTO);
    }

    private void enforceNotLocked(LeagueSeason season) {
        Instant lockTime = season.getLeaguePredictionLockTime();
        if (lockTime != null && Instant.now().isAfter(lockTime)) {
            throw new PredictionLockedException("League prediction window has closed for this season");
        }
    }

    private void saveTeamRankings(LeaguePrediction prediction, List<LeagueTeamPredictionDTO> rankings) {
        if (rankings == null) return;
        for (LeagueTeamPredictionDTO rankDto : rankings) {
            Team team = teamRepository.findByIdAndDeletedAtIsNull(rankDto.teamId())
                    .orElseThrow(() -> new ResourceNotFoundException("Team", rankDto.teamId()));
            LeagueTeamPrediction rankEntry = new LeagueTeamPrediction();
            rankEntry.setLeaguePrediction(prediction);
            rankEntry.setTeam(team);
            rankEntry.setPredictedRank(rankDto.predictedRank());
            rankEntry.setCreatedAt(Instant.now());
            teamPredictionRepository.save(rankEntry);
        }
    }

    private LeaguePredictionDTO convertToDTO(LeaguePrediction p) {
        List<LeagueTeamPredictionDTO> rankings = teamPredictionRepository
                .findByLeaguePredictionIdAndDeletedAtIsNull(p.getId())
                .stream()
                .map(r -> new LeagueTeamPredictionDTO(r.getId(), p.getId(),
                        r.getTeam().getId(), r.getTeam().getName(), r.getPredictedRank()))
                .toList();
        return new LeaguePredictionDTO(p.getId(), p.getSeason().getId(),
                p.getUser().getId(), p.getSubmittedAt(), rankings);
    }
}
