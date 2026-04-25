package com.divami.java_project.controller;

import com.divami.java_project.model.dto.LeagueSeasonDTO;
import com.divami.java_project.model.dto.StatusUpdateDTO;
import com.divami.java_project.service.LeagueSeasonService;
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
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
public class LeagueSeasonController {

    private static final Logger log = LoggerFactory.getLogger(LeagueSeasonController.class);

    private final LeagueSeasonService leagueSeasonService;

    public LeagueSeasonController(LeagueSeasonService leagueSeasonService) {
        this.leagueSeasonService = leagueSeasonService;
    }

    @GetMapping("/api/leagues/{leagueId}/seasons")
    public ResponseEntity<Page<LeagueSeasonDTO>> findByLeague(
            @PathVariable UUID leagueId,
            @ParameterObject @PageableDefault(size = 20, sort = "startTime") Pageable pageable) {
        return ResponseEntity.ok(leagueSeasonService.findByLeague(leagueId, pageable));
    }

    @GetMapping("/api/seasons/{id}")
    public ResponseEntity<LeagueSeasonDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(leagueSeasonService.findById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/leagues/{leagueId}/seasons")
    public ResponseEntity<LeagueSeasonDTO> create(
            @PathVariable UUID leagueId,
            @Valid @RequestBody LeagueSeasonDTO dto) {
        log.info("Creating season for leagueId={} name={}", leagueId, dto.seasonName());
        return ResponseEntity.status(HttpStatus.CREATED).body(leagueSeasonService.create(leagueId, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/seasons/{id}")
    public ResponseEntity<LeagueSeasonDTO> update(@PathVariable UUID id, @Valid @RequestBody LeagueSeasonDTO dto) {
        log.info("Updating season id={}", id);
        return ResponseEntity.ok(leagueSeasonService.update(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/api/seasons/{id}/status")
    public ResponseEntity<LeagueSeasonDTO> updateStatus(@PathVariable UUID id, @Valid @RequestBody StatusUpdateDTO dto) {
        log.info("Status update for season id={} to status={}", id, dto.status());
        return ResponseEntity.ok(leagueSeasonService.updateStatus(id, dto.status()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/seasons/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.info("Soft-delete season id={}", id);
        leagueSeasonService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
