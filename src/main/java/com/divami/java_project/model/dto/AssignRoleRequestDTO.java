package com.divami.java_project.model.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/** Request body for assigning a role to a user. */
public record AssignRoleRequestDTO(@NotNull UUID roleId) {}
