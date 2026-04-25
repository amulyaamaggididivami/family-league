package com.divami.java_project.service;

import com.divami.java_project.model.dto.EmailLogDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

/** Read-only query service for email audit records. Admin only. */
public interface EmailLogService {

    /** Filters by userId and/or eventType when provided; both are optional. */
    Page<EmailLogDTO> findAll(UUID userId, String eventType, Pageable pageable);

    EmailLogDTO findById(UUID id);
}
