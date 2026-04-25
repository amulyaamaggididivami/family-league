package com.divami.java_project.repository;

import com.divami.java_project.model.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerRepository extends JpaRepository<Player, UUID> {

    Page<Player> findByTeamIdAndDeletedAtIsNull(UUID teamId, Pageable pageable);

    Optional<Player> findByIdAndDeletedAtIsNull(UUID id);
}
