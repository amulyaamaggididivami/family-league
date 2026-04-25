package com.divami.java_project.service.impl;

import com.divami.java_project.exception.ResourceNotFoundException;
import com.divami.java_project.model.User;
import com.divami.java_project.model.dto.UserDTO;
import com.divami.java_project.repository.UserRepository;
import com.divami.java_project.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable) {
        return userRepository.findByDeletedAtIsNull(pageable).map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO findById(UUID id) {
        return convertToDTO(findUserOrThrow(id));
    }

    @Override
    public UserDTO updateProfile(UUID id, UserDTO dto) {
        User user = findUserOrThrow(id);
        user.setFullName(dto.fullName());
        user.setAvatarUrl(dto.avatarUrl());
        user.setUpdatedAt(Instant.now());
        log.info("Profile updated for userId={}", id);
        return convertToDTO(userRepository.save(user));
    }

    @Override
    public void delete(UUID id) {
        User user = findUserOrThrow(id);
        user.setDeletedAt(Instant.now());
        user.setActive(false);
        userRepository.save(user);
        log.info("Soft-deleted userId={}", id);
    }

    private User findUserOrThrow(UUID id) {
        return userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getAvatarUrl(),
                user.isActive(),
                user.getCreatedAt()
        );
    }
}
