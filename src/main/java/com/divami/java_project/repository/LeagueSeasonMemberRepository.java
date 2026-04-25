package com.divami.java_project.repository;

import com.divami.java_project.model.LeagueSeasonMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeagueSeasonMemberRepository extends JpaRepository<LeagueSeasonMember, UUID> {

    /** Paginated — used for the member list API endpoint. */
    Page<LeagueSeasonMember> findBySeasonIdAndDeletedAtIsNull(UUID seasonId, Pageable pageable);

    /** Full list — used by the notification scheduler to find who needs reminder emails. */
    List<LeagueSeasonMember> findBySeasonIdAndDeletedAtIsNull(UUID seasonId);

    Optional<LeagueSeasonMember> findBySeasonIdAndUserIdAndDeletedAtIsNull(UUID seasonId, UUID userId);

    Optional<LeagueSeasonMember> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsBySeasonIdAndUserIdAndDeletedAtIsNull(UUID seasonId, UUID userId);
}
