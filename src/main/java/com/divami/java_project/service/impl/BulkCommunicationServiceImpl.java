package com.divami.java_project.service.impl;

import com.divami.java_project.exception.ResourceNotFoundException;
import com.divami.java_project.model.BulkCommunicationCampaign;
import com.divami.java_project.model.User;
import com.divami.java_project.model.dto.BulkCommunicationCampaignDTO;
import com.divami.java_project.repository.BulkCommunicationCampaignRepository;
import com.divami.java_project.repository.UserRepository;
import com.divami.java_project.service.BulkCommunicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class BulkCommunicationServiceImpl implements BulkCommunicationService {

    private static final Logger log = LoggerFactory.getLogger(BulkCommunicationServiceImpl.class);

    private final BulkCommunicationCampaignRepository campaignRepository;
    private final UserRepository userRepository;

    public BulkCommunicationServiceImpl(BulkCommunicationCampaignRepository campaignRepository,
                                         UserRepository userRepository) {
        this.campaignRepository = campaignRepository;
        this.userRepository = userRepository;
    }

    @Override
    public BulkCommunicationCampaignDTO create(BulkCommunicationCampaignDTO dto, UUID adminId) {
        User admin = userRepository.findByIdAndDeletedAtIsNull(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("User", adminId));

        BulkCommunicationCampaign campaign = new BulkCommunicationCampaign();
        campaign.setAdmin(admin);
        campaign.setEventType(dto.eventType());
        campaign.setMessageTemplate(dto.messageTemplate());
        campaign.setUserFilter(dto.userFilter());
        campaign.setCreatedAt(Instant.now());
        campaign.setCreatedBy(adminId);

        BulkCommunicationCampaign saved = campaignRepository.save(campaign);

        // TODO: resolve recipients from userFilter criteria and enqueue individual emails in email_logs
        log.info("Admin {} created bulk campaign id={} eventType={}", adminId, saved.getId(), dto.eventType());
        return convertToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BulkCommunicationCampaignDTO> findAll(Pageable pageable) {
        return campaignRepository.findAllByOrderByCreatedAtDesc(pageable).map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public BulkCommunicationCampaignDTO findById(UUID id) {
        return convertToDTO(campaignRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BulkCommunicationCampaign", id)));
    }

    private BulkCommunicationCampaignDTO convertToDTO(BulkCommunicationCampaign c) {
        return new BulkCommunicationCampaignDTO(
                c.getId(),
                c.getAdmin().getId(),
                c.getEventType(),
                c.getMessageTemplate(),
                c.getUserFilter(),
                c.getRecipientsCount(),
                c.getSentAt(),
                c.getCreatedAt());
    }
}
