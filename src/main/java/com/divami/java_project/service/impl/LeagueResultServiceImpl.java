package com.divami.java_project.service.impl;

import com.divami.java_project.exception.BusinessException;
import com.divami.java_project.exception.DuplicateResourceException;
import com.divami.java_project.exception.ResourceNotFoundException;
import com.divami.java_project.model.LeagueResult;
import com.divami.java_project.model.LeagueSeason;
import com.divami.java_project.model.Team;
import com.divami.java_project.model.dto.LeagueResultDTO;
import com.divami.java_project.repository.LeagueResultRepository;
import com.divami.java_project.repository.LeagueSeasonRepository;
import com.divami.java_project.repository.TeamRepository;
import com.divami.java_project.service.LeagueResultService;
import com.divami.java_project.service.ScoringEngineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class LeagueResultServiceImpl implements LeagueResultService {

    private static final Logger log = LoggerFactory.getLogger(LeagueResultServiceImpl.class);

    private final LeagueResultRepository leagueResultRepository;
    private final LeagueSeasonRepository seasonRepository;
    private final TeamRepository teamRepository;
    private final ScoringEngineService scoringEngineService;

    public LeagueResultServiceImpl(LeagueResultRepository leagueResultRepository,
                                    LeagueSeasonRepository seasonRepository,
                                    TeamRepository teamRepository,
                                    ScoringEngineService scoringEngineService) {
        this.leagueResultRepository = leagueResultRepository;
        this.seasonRepository = seasonRepository;
        this.teamRepository = teamRepository;
        this.scoringEngineService = scoringEngineService;
    }

    @Override
    public LeagueResultDTO create(UUID seasonId, LeagueResultDTO dto, UUID adminId) {
        if (leagueResultRepository.existsBySeasonIdAndDeletedAtIsNull(seasonId)) {
            throw new DuplicateResourceException("A league result already exists for seasonId=" + seasonId);
        }
        LeagueSeason season = seasonRepository.findByIdAndDeletedAtIsNull(seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("LeagueSeason", seasonId));

        LeagueResult result = new LeagueResult();
        result.setSeason(season);
        applyPlacedTeams(result, dto);
        result.setCreatedAt(Instant.now());
        result.setCreatedBy(adminId);

        log.info("Admin {} entering league result for seasonId={}", adminId, seasonId);
        return convertToDTO(leagueResultRepository.save(result));
    }

    @Override
    @Transactional(readOnly = true)
    public LeagueResultDTO findBySeason(UUID seasonId) {
        return convertToDTO(leagueResultRepository.findBySeasonIdAndDeletedAtIsNull(seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("LeagueResult for season", seasonId)));
    }

    @Override
    public LeagueResultDTO update(UUID seasonId, LeagueResultDTO dto, UUID adminId) {
        LeagueResult result = leagueResultRepository.findBySeasonIdAndDeletedAtIsNull(seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("LeagueResult for season", seasonId));
        applyPlacedTeams(result, dto);
        result.setUpdatedAt(Instant.now());
        result.setUpdatedBy(adminId);

        log.info("Admin {} updated league result for seasonId={}", adminId, seasonId);
        return convertToDTO(leagueResultRepository.save(result));
    }

    @Override
    public LeagueResultDTO verify(UUID seasonId, UUID adminId) {
        LeagueResult result = leagueResultRepository.findBySeasonIdAndDeletedAtIsNull(seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("LeagueResult for season", seasonId));
        result.setVerifiedAt(Instant.now());
        result.setVerifiedBy(adminId);
        result.setUpdatedAt(Instant.now());
        result.setUpdatedBy(adminId);

        log.info("Admin {} verified league result for seasonId={}", adminId, seasonId);
        return convertToDTO(leagueResultRepository.save(result));
    }

    @Override
    public LeagueResultDTO publish(UUID seasonId, UUID adminId) {
        LeagueResult result = leagueResultRepository.findBySeasonIdAndDeletedAtIsNull(seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("LeagueResult for season", seasonId));
        if (result.getVerifiedAt() == null) {
            throw new BusinessException("League result must be verified before it can be published");
        }
        result.setPublishedAt(Instant.now());
        result.setPublishedBy(adminId);
        result.setUpdatedAt(Instant.now());
        result.setUpdatedBy(adminId);

        LeagueResultDTO saved = convertToDTO(leagueResultRepository.save(result));
        scoringEngineService.calculateLeaguePoints(seasonId);
        log.info("Admin {} published league result for seasonId={} — async scoring triggered", adminId, seasonId);
        return saved;
    }

    private void applyPlacedTeams(LeagueResult result, LeagueResultDTO dto) {
        result.setFirstPlaceTeam(resolveTeam(dto.firstPlaceTeamId()));
        result.setSecondPlaceTeam(resolveTeam(dto.secondPlaceTeamId()));
        result.setThirdPlaceTeam(resolveTeam(dto.thirdPlaceTeamId()));
    }

    private Team resolveTeam(UUID teamId) {
        if (teamId == null) return null;
        return teamRepository.findByIdAndDeletedAtIsNull(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team", teamId));
    }

    private LeagueResultDTO convertToDTO(LeagueResult r) {
        Team first = r.getFirstPlaceTeam();
        Team second = r.getSecondPlaceTeam();
        Team third = r.getThirdPlaceTeam();
        return new LeagueResultDTO(
                r.getId(),
                r.getSeason().getId(),
                first != null ? first.getId() : null,
                first != null ? first.getName() : null,
                second != null ? second.getId() : null,
                second != null ? second.getName() : null,
                third != null ? third.getId() : null,
                third != null ? third.getName() : null,
                r.getPublishedAt(),
                r.getVerifiedAt());
    }
}
