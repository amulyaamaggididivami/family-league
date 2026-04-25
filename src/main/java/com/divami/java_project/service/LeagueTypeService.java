package com.divami.java_project.service;

import com.divami.java_project.model.dto.LeagueTypeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

/** Manages tournament type master data — IPL, WPL, BBL, etc. */
public interface LeagueTypeService {

    Page<LeagueTypeDTO> findAll(Pageable pageable);

    LeagueTypeDTO findById(UUID id);

    LeagueTypeDTO create(LeagueTypeDTO dto);

    LeagueTypeDTO update(UUID id, LeagueTypeDTO dto);

    void delete(UUID id);
}
