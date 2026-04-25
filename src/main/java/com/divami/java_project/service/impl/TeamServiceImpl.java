package com.divami.java_project.service.impl;

import com.divami.java_project.exception.DuplicateResourceException;
import com.divami.java_project.exception.ResourceNotFoundException;
import com.divami.java_project.model.Team;
import com.divami.java_project.model.dto.TeamDTO;
import com.divami.java_project.repository.TeamRepository;
import com.divami.java_project.service.TeamService;
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
public class TeamServiceImpl implements TeamService {

    private static final Logger log = LoggerFactory.getLogger(TeamServiceImpl.class);

    private final TeamRepository teamRepository;

    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TeamDTO> findAll(String name, Pageable pageable) {
        if (name != null && !name.isBlank()) {
            return teamRepository.findByNameContainingIgnoreCaseAndDeletedAtIsNull(name, pageable).map(this::convertToDTO);
        }
        return teamRepository.findByDeletedAtIsNull(pageable).map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public TeamDTO findById(UUID id) {
        return convertToDTO(findOrThrow(id));
    }

    @Override
    public TeamDTO create(TeamDTO dto) {
        if (teamRepository.existsByShortCodeAndDeletedAtIsNull(dto.shortCode())) {
            throw new DuplicateResourceException("Short code already in use: " + dto.shortCode());
        }
        Team team = new Team();
        team.setName(dto.name());
        team.setShortCode(dto.shortCode());
        team.setLogoUrl(dto.logoUrl());
        team.setCreatedAt(Instant.now());
        log.info("Creating team name={}", dto.name());
        return convertToDTO(teamRepository.save(team));
    }

    @Override
    public TeamDTO update(UUID id, TeamDTO dto) {
        Team team = findOrThrow(id);
        team.setName(dto.name());
        team.setLogoUrl(dto.logoUrl());
        team.setUpdatedAt(Instant.now());
        return convertToDTO(teamRepository.save(team));
    }

    @Override
    public void delete(UUID id) {
        Team team = findOrThrow(id);
        team.setDeletedAt(Instant.now());
        teamRepository.save(team);
        log.info("Soft-deleted teamId={}", id);
    }

    private Team findOrThrow(UUID id) {
        return teamRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team", id));
    }

    private TeamDTO convertToDTO(Team t) {
        return new TeamDTO(t.getId(), t.getName(), t.getShortCode(), t.getLogoUrl());
    }
}
