package com.divami.java_project.service;

import com.divami.java_project.model.dto.AuditLogDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

/** Read-only query service for the audit trail. Admin only. */
public interface AuditLogService {

    /** Filters by entityType and/or entityId when provided; both are optional. */
    Page<AuditLogDTO> findAll(String entityType, UUID entityId, Pageable pageable);
}
