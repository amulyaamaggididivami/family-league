package com.divami.java_project.service;

import com.divami.java_project.model.dto.PlayerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

/** Manages players belonging to a team. */
public interface PlayerService {

    Page<PlayerDTO> findByTeam(UUID teamId, Pageable pageable);

    PlayerDTO findById(UUID id);

    PlayerDTO create(UUID teamId, PlayerDTO dto);

    PlayerDTO update(UUID id, PlayerDTO dto);

    void delete(UUID id);
}
