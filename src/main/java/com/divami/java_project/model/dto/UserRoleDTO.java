package com.divami.java_project.model.dto;

import java.time.Instant;
import java.util.UUID;

/** Transfer object for {@link com.divami.java_project.model.UserRole}. */
public record UserRoleDTO(
        UUID id,
        UUID userId,
        UUID roleId,
        String roleName,
        boolean isActive,
        Instant assignedAt
) {}
