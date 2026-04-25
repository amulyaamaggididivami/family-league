package com.divami.java_project.model.dto;

import java.time.Instant;
import java.util.UUID;

/** Transfer object for {@link com.divami.java_project.model.AuditLog}. Read-only — audit entries are system-generated. */
public record AuditLogDTO(
        UUID id,
        String entityType,
        UUID entityId,
        String action,
        UUID actorId,
        String actorUsername,
        String oldValue,
        String newValue,
        String ipAddress,
        Instant createdAt
) {}
