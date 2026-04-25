package com.divami.java_project.service;

import com.divami.java_project.model.dto.TeamDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

/** Manages the team master — records that are reused across seasons. */
public interface TeamService {

    /** Supports optional name-based search filter. */
    Page<TeamDTO> findAll(String name, Pageable pageable);

    TeamDTO findById(UUID id);

    TeamDTO create(TeamDTO dto);

    TeamDTO update(UUID id, TeamDTO dto);

    void delete(UUID id);
}
