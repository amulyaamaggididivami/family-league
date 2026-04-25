package com.divami.java_project.controller;

import com.divami.java_project.model.dto.PlayerDTO;
import com.divami.java_project.service.PlayerService;
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
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
public class PlayerController {

    private static final Logger log = LoggerFactory.getLogger(PlayerController.class);

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/api/teams/{teamId}/players")
    public ResponseEntity<Page<PlayerDTO>> findByTeam(
            @PathVariable UUID teamId,
            @ParameterObject @PageableDefault(size = 30, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(playerService.findByTeam(teamId, pageable));
    }

    @GetMapping("/api/players/{id}")
    public ResponseEntity<PlayerDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(playerService.findById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/teams/{teamId}/players")
    public ResponseEntity<PlayerDTO> create(
            @PathVariable UUID teamId,
            @Valid @RequestBody PlayerDTO dto) {
        log.info("Adding player name={} to teamId={}", dto.name(), teamId);
        return ResponseEntity.status(HttpStatus.CREATED).body(playerService.create(teamId, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/players/{id}")
    public ResponseEntity<PlayerDTO> update(@PathVariable UUID id, @Valid @RequestBody PlayerDTO dto) {
        log.info("Updating player id={}", id);
        return ResponseEntity.ok(playerService.update(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/players/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.info("Soft-delete player id={}", id);
        playerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
