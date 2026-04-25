package com.divami.java_project.model.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

/** Transfer object for {@link com.divami.java_project.model.Role}. */
public record RoleDTO(
        UUID id,
        @NotBlank String name,
        String description
) {}
