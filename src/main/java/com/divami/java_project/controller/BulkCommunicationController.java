package com.divami.java_project.controller;

import com.divami.java_project.model.dto.BulkCommunicationCampaignDTO;
import com.divami.java_project.service.BulkCommunicationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/api/bulk-communications")
public class BulkCommunicationController {

    private static final Logger log = LoggerFactory.getLogger(BulkCommunicationController.class);

    private final BulkCommunicationService bulkCommunicationService;

    public BulkCommunicationController(BulkCommunicationService bulkCommunicationService) {
        this.bulkCommunicationService = bulkCommunicationService;
    }

    @GetMapping
    public ResponseEntity<Page<BulkCommunicationCampaignDTO>> findAll(
            @ParameterObject @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(bulkCommunicationService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BulkCommunicationCampaignDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(bulkCommunicationService.findById(id));
    }

    @PostMapping
    public ResponseEntity<BulkCommunicationCampaignDTO> create(
            @Valid @RequestBody BulkCommunicationCampaignDTO dto,
            @RequestParam UUID adminId) {
        log.info("Admin {} creating bulk campaign eventType={}", adminId, dto.eventType());
        return ResponseEntity.status(HttpStatus.CREATED).body(bulkCommunicationService.create(dto, adminId));
    }
}
