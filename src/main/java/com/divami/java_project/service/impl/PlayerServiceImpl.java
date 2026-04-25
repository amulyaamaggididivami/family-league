package com.divami.java_project.service.impl;

import com.divami.java_project.exception.ResourceNotFoundException;
import com.divami.java_project.model.Player;
import com.divami.java_project.model.Team;
import com.divami.java_project.model.dto.PlayerDTO;
import com.divami.java_project.repository.PlayerRepository;
import com.divami.java_project.repository.TeamRepository;
import com.divami.java_project.service.PlayerService;
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
public class PlayerServiceImpl implements PlayerService {

    private static final Logger log = LoggerFactory.getLogger(PlayerServiceImpl.class);

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlayerDTO> findByTeam(UUID teamId, Pageable pageable) {
        return playerRepository.findByTeamIdAndDeletedAtIsNull(teamId, pageable).map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public PlayerDTO findById(UUID id) {
        return convertToDTO(findOrThrow(id));
    }

    @Override
    public PlayerDTO create(UUID teamId, PlayerDTO dto) {
        Team team = teamRepository.findByIdAndDeletedAtIsNull(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team", teamId));
        Player player = new Player();
        player.setTeam(team);
        player.setName(dto.name());
        player.setPosition(dto.position());
        player.setActive(dto.isActive());
        player.setCreatedAt(Instant.now());
        log.info("Adding player name={} to teamId={}", dto.name(), teamId);
        return convertToDTO(playerRepository.save(player));
    }

    @Override
    public PlayerDTO update(UUID id, PlayerDTO dto) {
        Player player = findOrThrow(id);
        player.setName(dto.name());
        player.setPosition(dto.position());
        player.setActive(dto.isActive());
        player.setUpdatedAt(Instant.now());
        return convertToDTO(playerRepository.save(player));
    }

    @Override
    public void delete(UUID id) {
        Player player = findOrThrow(id);
        player.setDeletedAt(Instant.now());
        player.setActive(false);
        playerRepository.save(player);
        log.info("Soft-deleted playerId={}", id);
    }

    private Player findOrThrow(UUID id) {
        return playerRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player", id));
    }

    private PlayerDTO convertToDTO(Player p) {
        return new PlayerDTO(p.getId(), p.getTeam().getId(), p.getTeam().getName(),
                p.getName(), p.getPosition(), p.isActive());
    }
}
