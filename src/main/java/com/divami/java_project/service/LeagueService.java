package com.divami.java_project.service;

import com.divami.java_project.model.dto.LeagueDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

/** Manages league editions — e.g. IPL 2025. */
public interface LeagueService {

    Page<LeagueDTO> findAll(Pageable pageable);

    /** Filters by league type when leagueTypeId is provided. */
    Page<LeagueDTO> findByLeagueType(UUID leagueTypeId, Pageable pageable);

    LeagueDTO findById(UUID id);

    LeagueDTO create(LeagueDTO dto);

    LeagueDTO update(UUID id, LeagueDTO dto);

    /** Transitions league status — e.g. ACTIVE → CLOSED. Throws BusinessException for invalid transitions. */
    LeagueDTO updateStatus(UUID id, String status);

    void delete(UUID id);
}
