package com.divami.java_project.controller;

import com.divami.java_project.model.dto.LeagueTypeDTO;
import com.divami.java_project.service.LeagueTypeService;
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
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/api/league-types")
public class LeagueTypeController {

    private static final Logger log = LoggerFactory.getLogger(LeagueTypeController.class);

    private final LeagueTypeService leagueTypeService;

    public LeagueTypeController(LeagueTypeService leagueTypeService) {
        this.leagueTypeService = leagueTypeService;
    }

    @GetMapping
    public ResponseEntity<Page<LeagueTypeDTO>> findAll(
            @ParameterObject @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(leagueTypeService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeagueTypeDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(leagueTypeService.findById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<LeagueTypeDTO> create(@Valid @RequestBody LeagueTypeDTO dto) {
        log.info("Creating league type name={}", dto.name());
        return ResponseEntity.status(HttpStatus.CREATED).body(leagueTypeService.create(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<LeagueTypeDTO> update(@PathVariable UUID id, @Valid @RequestBody LeagueTypeDTO dto) {
        log.info("Updating league type id={}", id);
        return ResponseEntity.ok(leagueTypeService.update(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.info("Soft-delete league type id={}", id);
        leagueTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
