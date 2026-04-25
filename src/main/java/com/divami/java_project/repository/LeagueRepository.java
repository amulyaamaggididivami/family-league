package com.divami.java_project.repository;

import com.divami.java_project.model.League;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeagueRepository extends JpaRepository<League, UUID> {

    Page<League> findByDeletedAtIsNull(Pageable pageable);

    Optional<League> findByIdAndDeletedAtIsNull(UUID id);

    Page<League> findByLeagueTypeIdAndDeletedAtIsNull(UUID leagueTypeId, Pageable pageable);
}
