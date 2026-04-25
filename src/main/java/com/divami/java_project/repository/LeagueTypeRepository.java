package com.divami.java_project.repository;

import com.divami.java_project.model.LeagueType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeagueTypeRepository extends JpaRepository<LeagueType, UUID> {

    Page<LeagueType> findByDeletedAtIsNull(Pageable pageable);

    Optional<LeagueType> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByNameAndDeletedAtIsNull(String name);

    boolean existsByShortCodeAndDeletedAtIsNull(String shortCode);
}
