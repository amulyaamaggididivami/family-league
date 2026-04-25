package com.divami.java_project.service.impl;

import com.divami.java_project.exception.BusinessException;
import com.divami.java_project.exception.ResourceNotFoundException;
import com.divami.java_project.model.League;
import com.divami.java_project.model.LeagueType;
import com.divami.java_project.model.dto.LeagueDTO;
import com.divami.java_project.repository.LeagueRepository;
import com.divami.java_project.repository.LeagueTypeRepository;
import com.divami.java_project.service.LeagueService;
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
public class LeagueServiceImpl implements LeagueService {

    private static final Logger log = LoggerFactory.getLogger(LeagueServiceImpl.class);

    private final LeagueRepository leagueRepository;
    private final LeagueTypeRepository leagueTypeRepository;

    public LeagueServiceImpl(LeagueRepository leagueRepository, LeagueTypeRepository leagueTypeRepository) {
        this.leagueRepository = leagueRepository;
        this.leagueTypeRepository = leagueTypeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeagueDTO> findAll(Pageable pageable) {
        return leagueRepository.findByDeletedAtIsNull(pageable).map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeagueDTO> findByLeagueType(UUID leagueTypeId, Pageable pageable) {
        return leagueRepository.findByLeagueTypeIdAndDeletedAtIsNull(leagueTypeId, pageable).map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public LeagueDTO findById(UUID id) {
        return convertToDTO(findOrThrow(id));
    }

    @Override
    public LeagueDTO create(LeagueDTO dto) {
        LeagueType leagueType = leagueTypeRepository.findByIdAndDeletedAtIsNull(dto.leagueTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("LeagueType", dto.leagueTypeId()));
        League league = new League();
        league.setLeagueType(leagueType);
        league.setName(dto.name());
        league.setSeasonYear(dto.seasonYear());
        league.setStatus(dto.status() != null ? dto.status() : "UPCOMING");
        league.setDescription(dto.description());
        league.setCreatedAt(Instant.now());
        log.info("Creating league name={} year={}", dto.name(), dto.seasonYear());
        return convertToDTO(leagueRepository.save(league));
    }

    @Override
    public LeagueDTO update(UUID id, LeagueDTO dto) {
        League league = findOrThrow(id);
        league.setName(dto.name());
        league.setSeasonYear(dto.seasonYear());
        league.setDescription(dto.description());
        league.setUpdatedAt(Instant.now());
        return convertToDTO(leagueRepository.save(league));
    }

    @Override
    public LeagueDTO updateStatus(UUID id, String status) {
        League league = findOrThrow(id);
        if ("CLOSED".equals(league.getStatus())) {
            throw new BusinessException("Closed leagues cannot be re-opened");
        }
        league.setStatus(status);
        league.setUpdatedAt(Instant.now());
        log.info("League id={} status changed to {}", id, status);
        return convertToDTO(leagueRepository.save(league));
    }

    @Override
    public void delete(UUID id) {
        League league = findOrThrow(id);
        league.setDeletedAt(Instant.now());
        leagueRepository.save(league);
        log.info("Soft-deleted leagueId={}", id);
    }

    private League findOrThrow(UUID id) {
        return leagueRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("League", id));
    }

    private LeagueDTO convertToDTO(League e) {
        return new LeagueDTO(e.getId(), e.getLeagueType().getId(), e.getLeagueType().getName(),
                e.getName(), e.getSeasonYear(), e.getStatus(), e.getDescription());
    }
}
