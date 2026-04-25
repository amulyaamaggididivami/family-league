package com.divami.java_project.controller;

import com.divami.java_project.model.dto.RoleDTO;
import com.divami.java_project.service.RoleService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

/**
 * CRUD for role master data (ROLE_USER, ROLE_ADMIN, etc.).
 * All endpoints are admin-only — enforced by Spring Security configuration.
 */
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private static final Logger log = LoggerFactory.getLogger(RoleController.class);

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<RoleDTO>> findAll() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(roleService.findById(id));
    }

    @PostMapping
    public ResponseEntity<RoleDTO> create(@Valid @RequestBody RoleDTO dto) {
        log.info("Creating role name={}", dto.name());
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> update(@PathVariable UUID id, @Valid @RequestBody RoleDTO dto) {
        log.info("Updating role id={}", id);
        return ResponseEntity.ok(roleService.update(id, dto));
    }

    /** Soft-deletes a role. Throws BusinessException if the role is still assigned to active users. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.info("Soft-delete role id={}", id);
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
