package com.divami.java_project.service;

import com.divami.java_project.model.dto.LeagueSeasonMemberDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

/** Controls season membership — joining and removal. */
public interface LeagueSeasonMemberService {

    /** Adds a user as a member of the season. Throws DuplicateResourceException if already a member. */
    LeagueSeasonMemberDTO join(UUID seasonId, UUID userId);

    Page<LeagueSeasonMemberDTO> findBySeason(UUID seasonId, Pageable pageable);

    /** Soft-removes a membership record. Admin only. */
    void remove(UUID memberId);
}
