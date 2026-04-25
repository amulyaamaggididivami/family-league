package com.divami.java_project.service.impl;

import com.divami.java_project.exception.BusinessException;
import com.divami.java_project.exception.DuplicateResourceException;
import com.divami.java_project.exception.PredictionLockedException;
import com.divami.java_project.exception.ResourceNotFoundException;
import com.divami.java_project.model.Match;
import com.divami.java_project.model.MatchPrediction;
import com.divami.java_project.model.Player;
import com.divami.java_project.model.Team;
import com.divami.java_project.model.User;
import com.divami.java_project.model.dto.MatchPredictionDTO;
import com.divami.java_project.repository.MatchPredictionRepository;
import com.divami.java_project.repository.MatchRepository;
import com.divami.java_project.repository.PlayerRepository;
import com.divami.java_project.repository.TeamRepository;
import com.divami.java_project.repository.UserRepository;
import com.divami.java_project.service.MatchPredictionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class MatchPredictionServiceImpl implements MatchPredictionService {

    private static final Logger log = LoggerFactory.getLogger(MatchPredictionServiceImpl.class);

    private final MatchPredictionRepository predictionRepository;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    public MatchPredictionServiceImpl(MatchPredictionRepository predictionRepository,
                                       MatchRepository matchRepository,
                                       UserRepository userRepository,
                                       TeamRepository teamRepository,
                                       PlayerRepository playerRepository) {
        this.predictionRepository = predictionRepository;
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public MatchPredictionDTO submit(UUID matchId, UUID userId, MatchPredictionDTO dto) {
        Match match = matchRepository.findByIdAndDeletedAtIsNull(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match", matchId));
        enforceNotLocked(match);

        if (predictionRepository.existsByMatchIdAndUserIdAndDeletedAtIsNull(matchId, userId)) {
            throw new DuplicateResourceException("User already has a prediction for this match");
        }
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        MatchPrediction prediction = new MatchPrediction();
        prediction.setMatch(match);
        prediction.setUser(user);
        applyPredictionFields(prediction, dto);
        prediction.setSubmittedAt(Instant.now());
        prediction.setCreatedAt(Instant.now());

        log.info("User {} submitted prediction for matchId={}", userId, matchId);
        return convertToDTO(predictionRepository.save(prediction));
    }

    @Override
    public MatchPredictionDTO update(UUID predictionId, UUID userId, MatchPredictionDTO dto) {
        MatchPrediction prediction = predictionRepository.findByIdAndDeletedAtIsNull(predictionId)
                .orElseThrow(() -> new ResourceNotFoundException("MatchPrediction", predictionId));
        enforceNotLocked(prediction.getMatch());

        applyPredictionFields(prediction, dto);
        prediction.setSubmittedAt(Instant.now());
        prediction.setUpdatedAt(Instant.now());

        log.info("User {} updated predictionId={}", userId, predictionId);
        return convertToDTO(predictionRepository.save(prediction));
    }

    @Override
    @Transactional(readOnly = true)
    public MatchPredictionDTO findMyPrediction(UUID matchId, UUID userId) {
        return convertToDTO(predictionRepository.findByMatchIdAndUserIdAndDeletedAtIsNull(matchId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("MatchPrediction for user", userId)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MatchPredictionDTO> findAllByMatch(UUID matchId, Pageable pageable) {
        Match match = matchRepository.findByIdAndDeletedAtIsNull(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match", matchId));
        if (Instant.now().isBefore(match.getPredictionLockTime())) {
            throw new BusinessException("Predictions are not visible until the prediction window closes");
        }
        return predictionRepository.findByMatchIdAndDeletedAtIsNull(matchId, pageable).map(this::convertToDTO);
    }

    private void enforceNotLocked(Match match) {
        if (Instant.now().isAfter(match.getPredictionLockTime())) {
            throw new PredictionLockedException("Prediction window has closed for this match");
        }
    }

    private void applyPredictionFields(MatchPrediction prediction, MatchPredictionDTO dto) {
        prediction.setPredictedWinner(dto.predictedWinnerId() != null
                ? teamRepository.findByIdAndDeletedAtIsNull(dto.predictedWinnerId())
                        .orElseThrow(() -> new ResourceNotFoundException("Team", dto.predictedWinnerId()))
                : null);
        prediction.setPredictedTossWinner(dto.predictedTossWinnerId() != null
                ? teamRepository.findByIdAndDeletedAtIsNull(dto.predictedTossWinnerId())
                        .orElseThrow(() -> new ResourceNotFoundException("Team", dto.predictedTossWinnerId()))
                : null);
        prediction.setPredictedPlayerOfMatch(dto.predictedPlayerOfMatchId() != null
                ? playerRepository.findByIdAndDeletedAtIsNull(dto.predictedPlayerOfMatchId())
                        .orElseThrow(() -> new ResourceNotFoundException("Player", dto.predictedPlayerOfMatchId()))
                : null);
    }

    private MatchPredictionDTO convertToDTO(MatchPrediction p) {
        Team winner = p.getPredictedWinner();
        Team toss = p.getPredictedTossWinner();
        Player pom = p.getPredictedPlayerOfMatch();
        return new MatchPredictionDTO(
                p.getId(),
                p.getMatch().getId(),
                p.getUser().getId(),
                winner != null ? winner.getId() : null,
                winner != null ? winner.getName() : null,
                toss != null ? toss.getId() : null,
                toss != null ? toss.getName() : null,
                pom != null ? pom.getId() : null,
                pom != null ? pom.getName() : null,
                p.getSubmittedAt());
    }
}
