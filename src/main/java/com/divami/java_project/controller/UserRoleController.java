package com.divami.java_project.controller;

import com.divami.java_project.model.dto.AssignRoleRequestDTO;
import com.divami.java_project.model.dto.UserRoleDTO;
import com.divami.java_project.service.UserRoleService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

/**
 * Assigns and revokes roles for a given user. Admin only.
 * Nested under /api/users/{userId}/roles to make the relationship explicit in the URL.
 */
@RestController
@RequestMapping("/api/users/{userId}/roles")
public class UserRoleController {

    private static final Logger log = LoggerFactory.getLogger(UserRoleController.class);

    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    /** Lists all active role assignments for a user. */
    @GetMapping
    public ResponseEntity<List<UserRoleDTO>> findByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(userRoleService.findByUser(userId));
    }

    /**
     * Assigns a role to a user.
     * The assignedBy UUID comes from the authenticated admin's JWT token in production.
     */
    @PostMapping
    public ResponseEntity<UserRoleDTO> assign(
            @PathVariable UUID userId,
            @Valid @RequestBody AssignRoleRequestDTO request,
            @RequestParam UUID assignedBy) {
        log.info("Assigning roleId={} to userId={}", request.roleId(), userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userRoleService.assign(userId, request.roleId(), assignedBy));
    }

    /** Soft-revokes a role assignment by setting deleted_at. */
    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> revoke(@PathVariable UUID userId, @PathVariable UUID roleId) {
        log.info("Revoking roleId={} from userId={}", roleId, userId);
        userRoleService.revoke(userId, roleId);
        return ResponseEntity.noContent().build();
    }
}
