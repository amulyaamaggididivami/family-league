package com.divami.java_project.controller;

import com.divami.java_project.model.dto.SeasonTeamDTO;
import com.divami.java_project.service.SeasonTeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/seasons/{seasonId}/teams")
public class SeasonTeamController {

    private static final Logger log = LoggerFactory.getLogger(SeasonTeamController.class);

    private final SeasonTeamService seasonTeamService;

    public SeasonTeamController(SeasonTeamService seasonTeamService) {
        this.seasonTeamService = seasonTeamService;
    }

    @GetMapping
    public ResponseEntity<List<SeasonTeamDTO>> findBySeason(@PathVariable UUID seasonId) {
        return ResponseEntity.ok(seasonTeamService.findBySeason(seasonId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<SeasonTeamDTO> addTeam(
            @PathVariable UUID seasonId,
            @RequestParam UUID teamId) {
        log.info("Adding teamId={} to seasonId={}", teamId, seasonId);
        return ResponseEntity.status(HttpStatus.CREATED).body(seasonTeamService.addTeamToSeason(seasonId, teamId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{seasonTeamId}")
    public ResponseEntity<Void> removeTeam(
            @PathVariable UUID seasonId,
            @PathVariable UUID seasonTeamId) {
        log.info("Removing seasonTeamId={} from seasonId={}", seasonTeamId, seasonId);
        seasonTeamService.removeFromSeason(seasonTeamId);
        return ResponseEntity.noContent().build();
    }
}
