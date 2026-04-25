package com.divami.java_project.repository;

import com.divami.java_project.model.MatchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MatchResultRepository extends JpaRepository<MatchResult, UUID> {

    Optional<MatchResult> findByMatchIdAndDeletedAtIsNull(UUID matchId);

    boolean existsByMatchIdAndDeletedAtIsNull(UUID matchId);
}
