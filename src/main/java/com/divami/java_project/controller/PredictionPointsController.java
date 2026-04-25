package com.divami.java_project.controller;

import com.divami.java_project.model.dto.PredictionPointsDTO;
import com.divami.java_project.service.PredictionPointsService;
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
@RequestMapping("/api")
public class PredictionPointsController {

    private final PredictionPointsService predictionPointsService;

    public PredictionPointsController(PredictionPointsService predictionPointsService) {
        this.predictionPointsService = predictionPointsService;
    }

    @GetMapping("/seasons/{seasonId}/points")
    public ResponseEntity<Page<PredictionPointsDTO>> findByUserAndSeason(
            @PathVariable UUID seasonId,
            @RequestParam UUID userId,
            @RequestParam(required = false) UUID matchId,
            @ParameterObject @PageableDefault(size = 20, sort = "calculatedAt") Pageable pageable) {
        return ResponseEntity.ok(predictionPointsService.findByUserAndSeason(userId, seasonId, matchId, pageable));
    }

    @GetMapping("/matches/{matchId}/points")
    public ResponseEntity<Page<PredictionPointsDTO>> findByMatch(
            @PathVariable UUID matchId,
            @ParameterObject @PageableDefault(size = 20, sort = "calculatedAt") Pageable pageable) {
        return ResponseEntity.ok(predictionPointsService.findByMatch(matchId, pageable));
    }
}
