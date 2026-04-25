package com.divami.java_project.controller;

import com.divami.java_project.model.dto.TeamDTO;
import com.divami.java_project.service.TeamService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private static final Logger log = LoggerFactory.getLogger(TeamController.class);

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public ResponseEntity<Page<TeamDTO>> findAll(
            @RequestParam(required = false) String name,
            @ParameterObject @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(teamService.findAll(name, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(teamService.findById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TeamDTO> create(@Valid @RequestBody TeamDTO dto) {
        log.info("Creating team name={}", dto.name());
        return ResponseEntity.status(HttpStatus.CREATED).body(teamService.create(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TeamDTO> update(@PathVariable UUID id, @Valid @RequestBody TeamDTO dto) {
        log.info("Updating team id={}", id);
        return ResponseEntity.ok(teamService.update(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.info("Soft-delete team id={}", id);
        teamService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
