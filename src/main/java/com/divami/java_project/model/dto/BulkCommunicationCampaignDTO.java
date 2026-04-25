package com.divami.java_project.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

/** Transfer object for {@link com.divami.java_project.model.BulkCommunicationCampaign}. */
public record BulkCommunicationCampaignDTO(
        UUID id,
        @NotNull UUID adminId,
        @NotBlank String eventType,
        String messageTemplate,
        String userFilter,
        Integer recipientsCount,
        Instant sentAt,
        Instant createdAt
) {}
