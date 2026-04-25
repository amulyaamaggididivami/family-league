package com.divami.java_project.repository;

import com.divami.java_project.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {

    List<UserRole> findByUserIdAndDeletedAtIsNull(UUID userId);

    Optional<UserRole> findByUserIdAndRoleIdAndDeletedAtIsNull(UUID userId, UUID roleId);

    boolean existsByUserIdAndRoleIdAndDeletedAtIsNull(UUID userId, UUID roleId);
}
