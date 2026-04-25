package com.divami.java_project.service.impl;

import com.divami.java_project.exception.DuplicateResourceException;
import com.divami.java_project.exception.ResourceNotFoundException;
import com.divami.java_project.model.Role;
import com.divami.java_project.model.User;
import com.divami.java_project.model.UserRole;
import com.divami.java_project.model.dto.UserRoleDTO;
import com.divami.java_project.repository.RoleRepository;
import com.divami.java_project.repository.UserRepository;
import com.divami.java_project.repository.UserRoleRepository;
import com.divami.java_project.service.UserRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserRoleServiceImpl implements UserRoleService {

    private static final Logger log = LoggerFactory.getLogger(UserRoleServiceImpl.class);

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository,
                                UserRepository userRepository,
                                RoleRepository roleRepository) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserRoleDTO> findByUser(UUID userId) {
        return userRoleRepository.findByUserIdAndDeletedAtIsNull(userId)
                .stream().map(this::convertToDTO).toList();
    }

    @Override
    public UserRoleDTO assign(UUID userId, UUID roleId, UUID assignedBy) {
        if (userRoleRepository.existsByUserIdAndRoleIdAndDeletedAtIsNull(userId, roleId)) {
            throw new DuplicateResourceException("Role already assigned to this user");
        }
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        Role role = roleRepository.findByIdAndDeletedAtIsNull(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", roleId));

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRole.setActive(true);
        userRole.setAssignedAt(Instant.now());
        userRole.setAssignedBy(assignedBy);

        log.info("Assigning role={} to userId={}", role.getName(), userId);
        return convertToDTO(userRoleRepository.save(userRole));
    }

    @Override
    public void revoke(UUID userId, UUID roleId) {
        UserRole userRole = userRoleRepository.findByUserIdAndRoleIdAndDeletedAtIsNull(userId, roleId)
                .orElseThrow(() -> new ResourceNotFoundException("UserRole", userId + "/" + roleId));
        userRole.setActive(false);
        userRole.setDeletedAt(Instant.now());
        userRoleRepository.save(userRole);
        log.info("Revoked roleId={} from userId={}", roleId, userId);
    }

    private UserRoleDTO convertToDTO(UserRole ur) {
        return new UserRoleDTO(
                ur.getId(),
                ur.getUser().getId(),
                ur.getRole().getId(),
                ur.getRole().getName(),
                ur.isActive(),
                ur.getAssignedAt()
        );
    }
}
