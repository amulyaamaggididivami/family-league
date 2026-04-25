package com.divami.java_project.repository;

import com.divami.java_project.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    List<Role> findByDeletedAtIsNull();

    Optional<Role> findByIdAndDeletedAtIsNull(UUID id);

    Optional<Role> findByNameAndDeletedAtIsNull(String name);

    boolean existsByNameAndDeletedAtIsNull(String name);
}
