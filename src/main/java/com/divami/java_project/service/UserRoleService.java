package com.divami.java_project.service;

import com.divami.java_project.model.dto.UserRoleDTO;
import java.util.List;
import java.util.UUID;

/** Assigns and revokes roles for users. Admin only. */
public interface UserRoleService {

    /** Assigns a role to a user. Throws DuplicateResourceException if already active. */
    UserRoleDTO assign(UUID userId, UUID roleId, UUID assignedBy);

    /** Soft-revokes a role from a user by setting deleted_at. */
    void revoke(UUID userId, UUID roleId);

    List<UserRoleDTO> findByUser(UUID userId);
}
