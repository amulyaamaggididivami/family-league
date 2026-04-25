package com.divami.java_project.controller;

import com.divami.java_project.model.dto.LeaguePredictionDTO;
import com.divami.java_project.service.LeaguePredictionService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/api/seasons/{seasonId}/league-predictions")
public class LeaguePredictionController {

    private static final Logger log = LoggerFactory.getLogger(LeaguePredictionController.class);

    private final LeaguePredictionService leaguePredictionService;

    public LeaguePredictionController(LeaguePredictionService leaguePredictionService) {
        this.leaguePredictionService = leaguePredictionService;
    }

    @GetMapping("/my")
    public ResponseEntity<LeaguePredictionDTO> findMyPrediction(
            @PathVariable UUID seasonId,
            @RequestParam UUID userId) {
        return ResponseEntity.ok(leaguePredictionService.findMyPrediction(seasonId, userId));
    }

    @GetMapping
    public ResponseEntity<Page<LeaguePredictionDTO>> findAll(
            @PathVariable UUID seasonId,
            @ParameterObject @PageableDefault(size = 20, sort = "submittedAt") Pageable pageable) {
        return ResponseEntity.ok(leaguePredictionService.findAllBySeason(seasonId, pageable));
    }

    @PostMapping
    public ResponseEntity<LeaguePredictionDTO> submit(
            @PathVariable UUID seasonId,
            @RequestParam UUID userId,
            @Valid @RequestBody LeaguePredictionDTO dto) {
        log.info("User {} submitting league prediction for seasonId={}", userId, seasonId);
        return ResponseEntity.status(HttpStatus.CREATED).body(leaguePredictionService.submit(seasonId, userId, dto));
    }

    @PutMapping("/{predictionId}")
    public ResponseEntity<LeaguePredictionDTO> update(
            @PathVariable UUID seasonId,
            @PathVariable UUID predictionId,
            @RequestParam UUID userId,
            @Valid @RequestBody LeaguePredictionDTO dto) {
        log.info("User {} updating league predictionId={} for seasonId={}", userId, predictionId, seasonId);
        return ResponseEntity.ok(leaguePredictionService.update(predictionId, userId, dto));
    }
}
