package com.divami.java_project.model.dto;

import java.time.Instant;
import java.util.UUID;

/** Transfer object for {@link com.divami.java_project.model.EmailLog}. */
public record EmailLogDTO(
        UUID id,
        UUID userId,
        UUID campaignId,
        String eventType,
        String subject,
        String status,
        String errorMessage,
        Instant sentAt,
        Instant scheduledAt
) {}
