package com.divami.java_project.service.impl;

import com.divami.java_project.exception.BusinessException;
import com.divami.java_project.exception.ResourceNotFoundException;
import com.divami.java_project.model.League;
import com.divami.java_project.model.LeagueSeason;
import com.divami.java_project.model.dto.LeagueSeasonDTO;
import com.divami.java_project.repository.LeagueRepository;
import com.divami.java_project.repository.LeagueSeasonRepository;
import com.divami.java_project.service.LeagueSeasonService;
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
public class LeagueSeasonServiceImpl implements LeagueSeasonService {

    private static final Logger log = LoggerFactory.getLogger(LeagueSeasonServiceImpl.class);

    private final LeagueSeasonRepository seasonRepository;
    private final LeagueRepository leagueRepository;

    public LeagueSeasonServiceImpl(LeagueSeasonRepository seasonRepository, LeagueRepository leagueRepository) {
        this.seasonRepository = seasonRepository;
        this.leagueRepository = leagueRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeagueSeasonDTO> findByLeague(UUID leagueId, Pageable pageable) {
        return seasonRepository.findByLeagueIdAndDeletedAtIsNull(leagueId, pageable).map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public LeagueSeasonDTO findById(UUID id) {
        return convertToDTO(findOrThrow(id));
    }

    @Override
    public LeagueSeasonDTO create(UUID leagueId, LeagueSeasonDTO dto) {
        League league = leagueRepository.findByIdAndDeletedAtIsNull(leagueId)
                .orElseThrow(() -> new ResourceNotFoundException("League", leagueId));
        LeagueSeason season = new LeagueSeason();
        season.setLeague(league);
        season.setSeasonName(dto.seasonName());
        season.setStatus(dto.status() != null ? dto.status() : "UPCOMING");
        season.setLeaguePredictionLockTime(dto.leaguePredictionLockTime());
        season.setStartTime(dto.startTime());
        season.setEndTime(dto.endTime());
        season.setCreatedAt(Instant.now());
        log.info("Creating season name={} for leagueId={}", dto.seasonName(), leagueId);
        return convertToDTO(seasonRepository.save(season));
    }

    @Override
    public LeagueSeasonDTO update(UUID id, LeagueSeasonDTO dto) {
        LeagueSeason season = findOrThrow(id);
        if ("CLOSED".equals(season.getStatus())) {
            throw new BusinessException("Cannot modify a closed season");
        }
        season.setSeasonName(dto.seasonName());
        season.setLeaguePredictionLockTime(dto.leaguePredictionLockTime());
        season.setStartTime(dto.startTime());
        season.setEndTime(dto.endTime());
        season.setUpdatedAt(Instant.now());
        return convertToDTO(seasonRepository.save(season));
    }

    @Override
    public LeagueSeasonDTO updateStatus(UUID id, String status) {
        LeagueSeason season = findOrThrow(id);
        if ("CLOSED".equals(season.getStatus())) {
            throw new BusinessException("Closed seasons cannot be re-opened");
        }
        season.setStatus(status);
        season.setUpdatedAt(Instant.now());
        log.info("Season id={} status changed to {}", id, status);
        return convertToDTO(seasonRepository.save(season));
    }

    @Override
    public void delete(UUID id) {
        LeagueSeason season = findOrThrow(id);
        season.setDeletedAt(Instant.now());
        seasonRepository.save(season);
        log.info("Soft-deleted seasonId={}", id);
    }

    private LeagueSeason findOrThrow(UUID id) {
        return seasonRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("LeagueSeason", id));
    }

    private LeagueSeasonDTO convertToDTO(LeagueSeason s) {
        return new LeagueSeasonDTO(s.getId(), s.getLeague().getId(), s.getLeague().getName(),
                s.getSeasonName(), s.getStatus(), s.getLeaguePredictionLockTime(),
                s.getStartTime(), s.getEndTime());
    }
}
