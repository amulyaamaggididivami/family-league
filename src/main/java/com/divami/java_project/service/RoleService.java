package com.divami.java_project.service;

import com.divami.java_project.model.dto.RoleDTO;
import java.util.List;
import java.util.UUID;

/** Manages role master data — ROLE_USER, ROLE_ADMIN, etc. Admin only. */
public interface RoleService {

    List<RoleDTO> findAll();

    RoleDTO findById(UUID id);

    RoleDTO create(RoleDTO dto);

    RoleDTO update(UUID id, RoleDTO dto);

    void delete(UUID id);
}
