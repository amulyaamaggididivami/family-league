package com.divami.java_project.service.impl;

import com.divami.java_project.exception.ResourceNotFoundException;
import com.divami.java_project.model.LeagueSeason;
import com.divami.java_project.model.Match;
import com.divami.java_project.model.Team;
import com.divami.java_project.model.dto.MatchDTO;
import com.divami.java_project.repository.LeagueSeasonRepository;
import com.divami.java_project.repository.MatchRepository;
import com.divami.java_project.repository.TeamRepository;
import com.divami.java_project.service.MatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class MatchServiceImpl implements MatchService {

    private static final Logger log = LoggerFactory.getLogger(MatchServiceImpl.class);

    private final MatchRepository matchRepository;
    private final LeagueSeasonRepository seasonRepository;
    private final TeamRepository teamRepository;

    @Value("${familyleague.prediction.match-lock-hours:1}")
    private long matchLockHours;

    public MatchServiceImpl(MatchRepository matchRepository,
                             LeagueSeasonRepository seasonRepository,
                             TeamRepository teamRepository) {
        this.matchRepository = matchRepository;
        this.seasonRepository = seasonRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MatchDTO> findBySeason(UUID seasonId, String status, Pageable pageable) {
        if (status != null && !status.isBlank()) {
            return matchRepository.findBySeasonIdAndStatusAndDeletedAtIsNull(seasonId, status, pageable)
                    .map(this::convertToDTO);
        }
        return matchRepository.findBySeasonIdAndDeletedAtIsNull(seasonId, pageable).map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public MatchDTO findById(UUID id) {
        return convertToDTO(findOrThrow(id));
    }

    @Override
    public MatchDTO create(UUID seasonId, MatchDTO dto) {
        LeagueSeason season = seasonRepository.findByIdAndDeletedAtIsNull(seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("LeagueSeason", seasonId));
        Team homeTeam = teamRepository.findByIdAndDeletedAtIsNull(dto.homeTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", dto.homeTeamId()));
        Team awayTeam = teamRepository.findByIdAndDeletedAtIsNull(dto.awayTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", dto.awayTeamId()));

        Match match = new Match();
        match.setSeason(season);
        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);
        match.setScheduledAt(dto.scheduledAt());
        match.setPredictionLockTime(dto.scheduledAt().minus(Duration.ofHours(matchLockHours)));
        match.setVenue(dto.venue());
        match.setStatus(dto.status() != null ? dto.status() : "SCHEDULED");
        match.setCreatedAt(Instant.now());

        log.info("Creating match seasonId={} home={} away={} scheduledAt={}", seasonId,
                homeTeam.getName(), awayTeam.getName(), dto.scheduledAt());
        return convertToDTO(matchRepository.save(match));
    }

    @Override
    public MatchDTO update(UUID id, MatchDTO dto) {
        Match match = findOrThrow(id);
        Team homeTeam = teamRepository.findByIdAndDeletedAtIsNull(dto.homeTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", dto.homeTeamId()));
        Team awayTeam = teamRepository.findByIdAndDeletedAtIsNull(dto.awayTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", dto.awayTeamId()));

        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);
        match.setScheduledAt(dto.scheduledAt());
        match.setPredictionLockTime(dto.scheduledAt().minus(Duration.ofHours(matchLockHours)));
        match.setVenue(dto.venue());
        match.setStatus(dto.status());
        match.setUpdatedAt(Instant.now());

        return convertToDTO(matchRepository.save(match));
    }

    @Override
    public void delete(UUID id) {
        Match match = findOrThrow(id);
        match.setDeletedAt(Instant.now());
        matchRepository.save(match);
        log.info("Soft-deleted matchId={}", id);
    }

    private Match findOrThrow(UUID id) {
        return matchRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match", id));
    }

    private MatchDTO convertToDTO(Match m) {
        return new MatchDTO(m.getId(), m.getSeason().getId(),
                m.getHomeTeam().getId(), m.getHomeTeam().getName(),
                m.getAwayTeam().getId(), m.getAwayTeam().getName(),
                m.getScheduledAt(), m.getPredictionLockTime(),
                m.getVenue(), m.getStatus());
    }
}
