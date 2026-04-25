package com.divami.java_project.controller;

import com.divami.java_project.model.dto.EmailLogDTO;
import com.divami.java_project.service.EmailLogService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/api/email-logs")
public class EmailLogController {

    private final EmailLogService emailLogService;

    public EmailLogController(EmailLogService emailLogService) {
        this.emailLogService = emailLogService;
    }

    @GetMapping
    public ResponseEntity<Page<EmailLogDTO>> findAll(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) String eventType,
            @ParameterObject @PageableDefault(size = 20, sort = "sentAt") Pageable pageable) {
        return ResponseEntity.ok(emailLogService.findAll(userId, eventType, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmailLogDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(emailLogService.findById(id));
    }
}
