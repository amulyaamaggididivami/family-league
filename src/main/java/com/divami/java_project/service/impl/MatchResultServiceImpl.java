package com.divami.java_project.service.impl;

import com.divami.java_project.exception.BusinessException;
import com.divami.java_project.exception.DuplicateResourceException;
import com.divami.java_project.exception.ResourceNotFoundException;
import com.divami.java_project.model.Match;
import com.divami.java_project.model.MatchResult;
import com.divami.java_project.model.Player;
import com.divami.java_project.model.Team;
import com.divami.java_project.model.dto.MatchResultDTO;
import com.divami.java_project.repository.MatchRepository;
import com.divami.java_project.repository.MatchResultRepository;
import com.divami.java_project.repository.PlayerRepository;
import com.divami.java_project.repository.TeamRepository;
import com.divami.java_project.service.MatchResultService;
import com.divami.java_project.service.ScoringEngineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class MatchResultServiceImpl implements MatchResultService {

    private static final Logger log = LoggerFactory.getLogger(MatchResultServiceImpl.class);

    private final MatchResultRepository matchResultRepository;
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final ScoringEngineService scoringEngineService;

    public MatchResultServiceImpl(MatchResultRepository matchResultRepository,
                                   MatchRepository matchRepository,
                                   TeamRepository teamRepository,
                                   PlayerRepository playerRepository,
                                   ScoringEngineService scoringEngineService) {
        this.matchResultRepository = matchResultRepository;
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.scoringEngineService = scoringEngineService;
    }

    @Override
    public MatchResultDTO create(UUID matchId, MatchResultDTO dto, UUID adminId) {
        if (matchResultRepository.existsByMatchIdAndDeletedAtIsNull(matchId)) {
            throw new DuplicateResourceException("A result already exists for matchId=" + matchId);
        }
        Match match = matchRepository.findByIdAndDeletedAtIsNull(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match", matchId));

        MatchResult result = new MatchResult();
        result.setMatch(match);
        result.setTie(dto.isTie());
        applyTeamsAndPlayer(result, dto);
        result.setCreatedAt(Instant.now());
        result.setCreatedBy(adminId);

        log.info("Admin {} entering result for matchId={}", adminId, matchId);
        return convertToDTO(matchResultRepository.save(result));
    }

    @Override
    @Transactional(readOnly = true)
    public MatchResultDTO findByMatch(UUID matchId) {
        return convertToDTO(matchResultRepository.findByMatchIdAndDeletedAtIsNull(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("MatchResult for match", matchId)));
    }

    @Override
    public MatchResultDTO update(UUID matchId, MatchResultDTO dto, UUID adminId) {
        MatchResult result = matchResultRepository.findByMatchIdAndDeletedAtIsNull(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("MatchResult for match", matchId));
        result.setTie(dto.isTie());
        applyTeamsAndPlayer(result, dto);
        result.setUpdatedAt(Instant.now());
        result.setUpdatedBy(adminId);

        log.info("Admin {} updated result for matchId={}", adminId, matchId);
        return convertToDTO(matchResultRepository.save(result));
    }

    @Override
    public MatchResultDTO verify(UUID matchId, UUID adminId) {
        MatchResult result = matchResultRepository.findByMatchIdAndDeletedAtIsNull(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("MatchResult for match", matchId));
        result.setVerifiedAt(Instant.now());
        result.setVerifiedBy(adminId);
        result.setUpdatedAt(Instant.now());
        result.setUpdatedBy(adminId);

        log.info("Admin {} verified result for matchId={}", adminId, matchId);
        return convertToDTO(matchResultRepository.save(result));
    }

    @Override
    public MatchResultDTO publish(UUID matchId, UUID adminId) {
        MatchResult result = matchResultRepository.findByMatchIdAndDeletedAtIsNull(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("MatchResult for match", matchId));
        if (result.getVerifiedAt() == null) {
            throw new BusinessException("Match result must be verified before it can be published");
        }
        result.setPublishedAt(Instant.now());
        result.setPublishedBy(adminId);
        result.setUpdatedAt(Instant.now());
        result.setUpdatedBy(adminId);

        MatchResultDTO saved = convertToDTO(matchResultRepository.save(result));
        scoringEngineService.calculateMatchPoints(matchId);
        log.info("Admin {} published result for matchId={} — async scoring triggered", adminId, matchId);
        return saved;
    }

    private void applyTeamsAndPlayer(MatchResult result, MatchResultDTO dto) {
        result.setWinningTeam(dto.winningTeamId() != null
                ? teamRepository.findByIdAndDeletedAtIsNull(dto.winningTeamId())
                        .orElseThrow(() -> new ResourceNotFoundException("Team", dto.winningTeamId()))
                : null);
        result.setTossWinnerTeam(dto.tossWinnerTeamId() != null
                ? teamRepository.findByIdAndDeletedAtIsNull(dto.tossWinnerTeamId())
                        .orElseThrow(() -> new ResourceNotFoundException("Team", dto.tossWinnerTeamId()))
                : null);
        result.setPlayerOfMatch(dto.playerOfMatchId() != null
                ? playerRepository.findByIdAndDeletedAtIsNull(dto.playerOfMatchId())
                        .orElseThrow(() -> new ResourceNotFoundException("Player", dto.playerOfMatchId()))
                : null);
    }

    private MatchResultDTO convertToDTO(MatchResult r) {
        Team winner = r.getWinningTeam();
        Team toss = r.getTossWinnerTeam();
        Player pom = r.getPlayerOfMatch();
        return new MatchResultDTO(
                r.getId(),
                r.getMatch().getId(),
                winner != null ? winner.getId() : null,
                winner != null ? winner.getName() : null,
                toss != null ? toss.getId() : null,
                toss != null ? toss.getName() : null,
                pom != null ? pom.getId() : null,
                pom != null ? pom.getName() : null,
                r.isTie(),
                r.getPublishedAt(),
                r.getVerifiedAt());
    }
}
