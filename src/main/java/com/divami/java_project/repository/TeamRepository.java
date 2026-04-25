package com.divami.java_project.repository;

import com.divami.java_project.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {

    Page<Team> findByDeletedAtIsNull(Pageable pageable);

    Optional<Team> findByIdAndDeletedAtIsNull(UUID id);

    /** Name-based search — case-insensitive partial match, excludes soft-deleted. */
    Page<Team> findByNameContainingIgnoreCaseAndDeletedAtIsNull(String name, Pageable pageable);

    boolean existsByShortCodeAndDeletedAtIsNull(String shortCode);
}
