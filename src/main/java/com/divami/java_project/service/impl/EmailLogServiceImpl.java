package com.divami.java_project.service.impl;

import com.divami.java_project.exception.ResourceNotFoundException;
import com.divami.java_project.model.EmailLog;
import com.divami.java_project.model.dto.EmailLogDTO;
import com.divami.java_project.repository.EmailLogRepository;
import com.divami.java_project.service.EmailLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class EmailLogServiceImpl implements EmailLogService {

    private final EmailLogRepository emailLogRepository;

    public EmailLogServiceImpl(EmailLogRepository emailLogRepository) {
        this.emailLogRepository = emailLogRepository;
    }

    @Override
    public Page<EmailLogDTO> findAll(UUID userId, String eventType, Pageable pageable) {
        if (userId != null && eventType != null) {
            return emailLogRepository.findByUserIdAndEventTypeAndDeletedAtIsNull(userId, eventType, pageable)
                    .map(this::convertToDTO);
        }
        if (userId != null) {
            return emailLogRepository.findByUserIdAndDeletedAtIsNull(userId, pageable).map(this::convertToDTO);
        }
        if (eventType != null) {
            return emailLogRepository.findByEventTypeAndDeletedAtIsNull(eventType, pageable).map(this::convertToDTO);
        }
        return emailLogRepository.findByDeletedAtIsNull(pageable).map(this::convertToDTO);
    }

    @Override
    public EmailLogDTO findById(UUID id) {
        return convertToDTO(emailLogRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("EmailLog", id)));
    }

    private EmailLogDTO convertToDTO(EmailLog e) {
        return new EmailLogDTO(
                e.getId(),
                e.getUser().getId(),
                e.getCampaign() != null ? e.getCampaign().getId() : null,
                e.getEventType(),
                e.getSubject(),
                e.getStatus(),
                e.getErrorMessage(),
                e.getSentAt(),
                e.getScheduledAt());
    }
}
