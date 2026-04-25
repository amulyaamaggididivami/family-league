package com.divami.java_project.controller;

import com.divami.java_project.model.dto.LeagueResultDTO;
import com.divami.java_project.service.LeagueResultService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

/**
 * Admin workflow for final league standings: enter → verify → publish.
 * Publishing triggers the final leaderboard calculation and closes the season for predictions.
 * Mirrors the MatchResultController workflow — verify is mandatory before publish.
 */
@RestController
@RequestMapping("/api/seasons/{seasonId}/result")
public class LeagueResultController {

    private static final Logger log = LoggerFactory.getLogger(LeagueResultController.class);

    private final LeagueResultService leagueResultService;

    public LeagueResultController(LeagueResultService leagueResultService) {
        this.leagueResultService = leagueResultService;
    }

    /** Returns the final league result for a season if published. */
    @GetMapping
    public ResponseEntity<LeagueResultDTO> findBySeason(@PathVariable UUID seasonId) {
        return ResponseEntity.ok(leagueResultService.findBySeason(seasonId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<LeagueResultDTO> create(
            @PathVariable UUID seasonId,
            @Valid @RequestBody LeagueResultDTO dto,
            @RequestParam UUID adminId) {
        log.info("Admin {} entering league result for seasonId={}", adminId, seasonId);
        return ResponseEntity.status(HttpStatus.CREATED).body(leagueResultService.create(seasonId, dto, adminId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<LeagueResultDTO> update(
            @PathVariable UUID seasonId,
            @Valid @RequestBody LeagueResultDTO dto,
            @RequestParam UUID adminId) {
        log.info("Admin {} updating league result for seasonId={}", adminId, seasonId);
        return ResponseEntity.ok(leagueResultService.update(seasonId, dto, adminId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/verify")
    public ResponseEntity<LeagueResultDTO> verify(
            @PathVariable UUID seasonId,
            @RequestParam UUID adminId) {
        log.info("Admin {} verifying league result for seasonId={}", adminId, seasonId);
        return ResponseEntity.ok(leagueResultService.verify(seasonId, adminId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/publish")
    public ResponseEntity<LeagueResultDTO> publish(
            @PathVariable UUID seasonId,
            @RequestParam UUID adminId) {
        log.info("Admin {} publishing league result for seasonId={}", adminId, seasonId);
        return ResponseEntity.ok(leagueResultService.publish(seasonId, adminId));
    }
}
