package com.divami.java_project.service;

import com.divami.java_project.model.dto.BulkCommunicationCampaignDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

/** Admin service for creating and tracking bulk email communication campaigns. */
public interface BulkCommunicationService {

    /**
     * Creates a campaign, resolves recipients from the userFilter criteria,
     * and enqueues individual emails — recorded in email_logs with this campaign_id.
     */
    BulkCommunicationCampaignDTO create(BulkCommunicationCampaignDTO dto, UUID adminId);

    Page<BulkCommunicationCampaignDTO> findAll(Pageable pageable);

    BulkCommunicationCampaignDTO findById(UUID id);
}
