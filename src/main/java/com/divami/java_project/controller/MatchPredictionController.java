package com.divami.java_project.controller;

import com.divami.java_project.model.dto.MatchPredictionDTO;
import com.divami.java_project.service.MatchPredictionService;
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
@RequestMapping("/api/matches/{matchId}/predictions")
public class MatchPredictionController {

    private static final Logger log = LoggerFactory.getLogger(MatchPredictionController.class);

    private final MatchPredictionService matchPredictionService;

    public MatchPredictionController(MatchPredictionService matchPredictionService) {
        this.matchPredictionService = matchPredictionService;
    }

    @GetMapping("/my")
    public ResponseEntity<MatchPredictionDTO> findMyPrediction(
            @PathVariable UUID matchId,
            @RequestParam UUID userId) {
        return ResponseEntity.ok(matchPredictionService.findMyPrediction(matchId, userId));
    }

    @GetMapping
    public ResponseEntity<Page<MatchPredictionDTO>> findAll(
            @PathVariable UUID matchId,
            @ParameterObject @PageableDefault(size = 20, sort = "submittedAt") Pageable pageable) {
        return ResponseEntity.ok(matchPredictionService.findAllByMatch(matchId, pageable));
    }

    @PostMapping
    public ResponseEntity<MatchPredictionDTO> submit(
            @PathVariable UUID matchId,
            @RequestParam UUID userId,
            @Valid @RequestBody MatchPredictionDTO dto) {
        log.info("User {} submitting prediction for matchId={}", userId, matchId);
        return ResponseEntity.status(HttpStatus.CREATED).body(matchPredictionService.submit(matchId, userId, dto));
    }

    @PutMapping("/{predictionId}")
    public ResponseEntity<MatchPredictionDTO> update(
            @PathVariable UUID matchId,
            @PathVariable UUID predictionId,
            @RequestParam UUID userId,
            @Valid @RequestBody MatchPredictionDTO dto) {
        log.info("User {} updating predictionId={} for matchId={}", userId, predictionId, matchId);
        return ResponseEntity.ok(matchPredictionService.update(predictionId, userId, dto));
    }
}
