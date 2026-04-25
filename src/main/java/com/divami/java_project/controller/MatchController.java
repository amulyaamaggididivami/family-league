package com.divami.java_project.controller;

import com.divami.java_project.model.dto.MatchDTO;
import com.divami.java_project.service.MatchService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
public class MatchController {

    private static final Logger log = LoggerFactory.getLogger(MatchController.class);

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/api/seasons/{seasonId}/matches")
    public ResponseEntity<Page<MatchDTO>> findBySeason(
            @PathVariable UUID seasonId,
            @RequestParam(required = false) String status,
            @ParameterObject @PageableDefault(size = 20, sort = "scheduledAt") Pageable pageable) {
        return ResponseEntity.ok(matchService.findBySeason(seasonId, status, pageable));
    }

    @GetMapping("/api/matches/{id}")
    public ResponseEntity<MatchDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(matchService.findById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/seasons/{seasonId}/matches")
    public ResponseEntity<MatchDTO> create(
            @PathVariable UUID seasonId,
            @Valid @RequestBody MatchDTO dto) {
        log.info("Creating match in seasonId={} home={} away={}", seasonId, dto.homeTeamId(), dto.awayTeamId());
        return ResponseEntity.status(HttpStatus.CREATED).body(matchService.create(seasonId, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/matches/{id}")
    public ResponseEntity<MatchDTO> update(@PathVariable UUID id, @Valid @RequestBody MatchDTO dto) {
        log.info("Updating match id={}", id);
        return ResponseEntity.ok(matchService.update(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/matches/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.info("Soft-delete match id={}", id);
        matchService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
