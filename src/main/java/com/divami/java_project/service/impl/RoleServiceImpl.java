package com.divami.java_project.service.impl;

import com.divami.java_project.exception.DuplicateResourceException;
import com.divami.java_project.exception.ResourceNotFoundException;
import com.divami.java_project.model.Role;
import com.divami.java_project.model.dto.RoleDTO;
import com.divami.java_project.repository.RoleRepository;
import com.divami.java_project.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private static final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> findAll() {
        return roleRepository.findByDeletedAtIsNull().stream().map(this::convertToDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDTO findById(UUID id) {
        return convertToDTO(findRoleOrThrow(id));
    }

    @Override
    public RoleDTO create(RoleDTO dto) {
        if (roleRepository.existsByNameAndDeletedAtIsNull(dto.name())) {
            throw new DuplicateResourceException("Role already exists: " + dto.name());
        }
        Role role = new Role();
        role.setName(dto.name());
        role.setDescription(dto.description());
        role.setCreatedAt(Instant.now());
        log.info("Creating role name={}", dto.name());
        return convertToDTO(roleRepository.save(role));
    }

    @Override
    public RoleDTO update(UUID id, RoleDTO dto) {
        Role role = findRoleOrThrow(id);
        role.setDescription(dto.description());
        role.setUpdatedAt(Instant.now());
        return convertToDTO(roleRepository.save(role));
    }

    @Override
    public void delete(UUID id) {
        Role role = findRoleOrThrow(id);
        role.setDeletedAt(Instant.now());
        roleRepository.save(role);
        log.info("Soft-deleted roleId={}", id);
    }

    private Role findRoleOrThrow(UUID id) {
        return roleRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", id));
    }

    private RoleDTO convertToDTO(Role role) {
        return new RoleDTO(role.getId(), role.getName(), role.getDescription());
    }
}
