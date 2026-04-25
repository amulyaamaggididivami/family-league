package com.divami.java_project.controller;

import com.divami.java_project.model.dto.MatchResultDTO;
import com.divami.java_project.service.MatchResultService;
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
 * Admin workflow for match results: enter → verify → publish.
 * Publishing triggers async point calculation and leaderboard snapshot creation.
 * The verify step is mandatory before publishing to prevent accidental publication of incorrect data.
 */
@RestController
@RequestMapping("/api/matches/{matchId}/result")
public class MatchResultController {

    private static final Logger log = LoggerFactory.getLogger(MatchResultController.class);

    private final MatchResultService matchResultService;

    public MatchResultController(MatchResultService matchResultService) {
        this.matchResultService = matchResultService;
    }

    /** Returns the result for a match if it has been entered. */
    @GetMapping
    public ResponseEntity<MatchResultDTO> findByMatch(@PathVariable UUID matchId) {
        return ResponseEntity.ok(matchResultService.findByMatch(matchId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<MatchResultDTO> create(
            @PathVariable UUID matchId,
            @Valid @RequestBody MatchResultDTO dto,
            @RequestParam UUID adminId) {
        log.info("Admin {} entering result for matchId={}", adminId, matchId);
        return ResponseEntity.status(HttpStatus.CREATED).body(matchResultService.create(matchId, dto, adminId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<MatchResultDTO> update(
            @PathVariable UUID matchId,
            @Valid @RequestBody MatchResultDTO dto,
            @RequestParam UUID adminId) {
        log.info("Admin {} updating result for matchId={}", adminId, matchId);
        return ResponseEntity.ok(matchResultService.update(matchId, dto, adminId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/verify")
    public ResponseEntity<MatchResultDTO> verify(
            @PathVariable UUID matchId,
            @RequestParam UUID adminId) {
        log.info("Admin {} verifying result for matchId={}", adminId, matchId);
        return ResponseEntity.ok(matchResultService.verify(matchId, adminId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/publish")
    public ResponseEntity<MatchResultDTO> publish(
            @PathVariable UUID matchId,
            @RequestParam UUID adminId) {
        log.info("Admin {} publishing result for matchId={}", adminId, matchId);
        return ResponseEntity.ok(matchResultService.publish(matchId, adminId));
    }
}
