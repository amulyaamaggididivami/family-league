package com.divami.java_project.service.impl;

import com.divami.java_project.model.AuditLog;
import com.divami.java_project.model.dto.AuditLogDTO;
import com.divami.java_project.repository.AuditLogRepository;
import com.divami.java_project.service.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public Page<AuditLogDTO> findAll(String entityType, UUID entityId, Pageable pageable) {
        if (entityType != null && entityId != null) {
            return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId, pageable)
                    .map(this::convertToDTO);
        }
        if (entityType != null) {
            return auditLogRepository.findByEntityType(entityType, pageable).map(this::convertToDTO);
        }
        if (entityId != null) {
            return auditLogRepository.findByEntityId(entityId, pageable).map(this::convertToDTO);
        }
        return auditLogRepository.findAll(pageable).map(this::convertToDTO);
    }

    private AuditLogDTO convertToDTO(AuditLog a) {
        return new AuditLogDTO(
                a.getId(),
                a.getEntityType(),
                a.getEntityId(),
                a.getAction(),
                a.getActor() != null ? a.getActor().getId() : null,
                a.getActor() != null ? a.getActor().getUsername() : null,
                a.getOldValue(),
                a.getNewValue(),
                a.getIpAddress(),
                a.getCreatedAt());
    }
}
