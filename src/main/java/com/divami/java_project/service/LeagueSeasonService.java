package com.divami.java_project.service;

import com.divami.java_project.model.dto.LeagueSeasonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

/** Manages seasons (phases) within a league edition. */
public interface LeagueSeasonService {

    Page<LeagueSeasonDTO> findByLeague(UUID leagueId, Pageable pageable);

    LeagueSeasonDTO findById(UUID id);

    LeagueSeasonDTO create(UUID leagueId, LeagueSeasonDTO dto);

    LeagueSeasonDTO update(UUID id, LeagueSeasonDTO dto);

    /** Transitions season status and enforces business rules (e.g. can't reopen a CLOSED season). */
    LeagueSeasonDTO updateStatus(UUID id, String status);

    void delete(UUID id);
}
