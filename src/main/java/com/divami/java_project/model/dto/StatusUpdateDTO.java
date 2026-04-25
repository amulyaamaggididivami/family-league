package com.divami.java_project.model.dto;

import jakarta.validation.constraints.NotBlank;

/** Generic request body for PATCH status-change endpoints on leagues, seasons, and matches. */
public record StatusUpdateDTO(@NotBlank String status) {}
