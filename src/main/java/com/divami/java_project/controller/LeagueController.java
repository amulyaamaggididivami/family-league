package com.divami.java_project.controller;

import com.divami.java_project.model.dto.LeagueDTO;
import com.divami.java_project.model.dto.StatusUpdateDTO;
import com.divami.java_project.service.LeagueService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/api/leagues")
public class LeagueController {

    private static final Logger log = LoggerFactory.getLogger(LeagueController.class);

    private final LeagueService leagueService;

    public LeagueController(LeagueService leagueService) {
        this.leagueService = leagueService;
    }

    @GetMapping
    public ResponseEntity<Page<LeagueDTO>> findAll(
            @RequestParam(required = false) UUID leagueTypeId,
            @ParameterObject @PageableDefault(size = 20, sort = "seasonYear") Pageable pageable) {
        if (leagueTypeId != null) {
            return ResponseEntity.ok(leagueService.findByLeagueType(leagueTypeId, pageable));
        }
        return ResponseEntity.ok(leagueService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeagueDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(leagueService.findById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<LeagueDTO> create(@Valid @RequestBody LeagueDTO dto) {
        log.info("Creating league name={} year={}", dto.name(), dto.seasonYear());
        return ResponseEntity.status(HttpStatus.CREATED).body(leagueService.create(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<LeagueDTO> update(@PathVariable UUID id, @Valid @RequestBody LeagueDTO dto) {
        log.info("Updating league id={}", id);
        return ResponseEntity.ok(leagueService.update(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<LeagueDTO> updateStatus(@PathVariable UUID id, @Valid @RequestBody StatusUpdateDTO dto) {
        log.info("Status update for league id={} to status={}", id, dto.status());
        return ResponseEntity.ok(leagueService.updateStatus(id, dto.status()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.info("Soft-delete league id={}", id);
        leagueService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
