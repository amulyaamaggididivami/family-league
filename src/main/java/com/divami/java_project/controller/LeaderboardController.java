package com.divami.java_project.controller;

import com.divami.java_project.model.dto.LeaderboardSnapshotDTO;
import com.divami.java_project.service.LeaderboardService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/seasons/{seasonId}/leaderboard")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @GetMapping
    public ResponseEntity<List<LeaderboardSnapshotDTO>> findCurrent(@PathVariable UUID seasonId) {
        return ResponseEntity.ok(leaderboardService.findCurrentBySeason(seasonId));
    }

    @GetMapping("/history")
    public ResponseEntity<Page<LeaderboardSnapshotDTO>> findHistory(
            @PathVariable UUID seasonId,
            @ParameterObject @PageableDefault(size = 50, sort = "snapshotAt") Pageable pageable) {
        return ResponseEntity.ok(leaderboardService.findSnapshotHistory(seasonId, pageable));
    }
}
