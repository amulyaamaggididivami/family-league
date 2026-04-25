package com.divami.java_project.service.impl;

import com.divami.java_project.exception.DuplicateResourceException;
import com.divami.java_project.exception.BusinessException;
import com.divami.java_project.model.Role;
import com.divami.java_project.model.User;
import com.divami.java_project.model.UserRole;
import com.divami.java_project.model.dto.AuthResponseDTO;
import com.divami.java_project.model.dto.LoginRequestDTO;
import com.divami.java_project.model.dto.RegisterRequestDTO;
import com.divami.java_project.repository.RoleRepository;
import com.divami.java_project.repository.UserRepository;
import com.divami.java_project.repository.UserRoleRepository;
import com.divami.java_project.security.JwtProvider;
import com.divami.java_project.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthServiceImpl(UserRepository userRepository,
                            RoleRepository roleRepository,
                            UserRoleRepository userRoleRepository,
                            PasswordEncoder passwordEncoder,
                            JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.existsByUsernameAndDeletedAtIsNull(request.username())) {
            throw new DuplicateResourceException("Username already taken: " + request.username());
        }
        if (userRepository.existsByEmailAndDeletedAtIsNull(request.email())) {
            throw new DuplicateResourceException("Email already registered: " + request.email());
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setFullName(request.fullName());
        user.setActive(true);
        user.setCreatedAt(Instant.now());

        user = userRepository.save(user);
        log.info("Registered new user id={} username={}", user.getId(), user.getUsername());

        Role userRole = roleRepository.findByNameAndDeletedAtIsNull("ROLE_USER")
                .orElseThrow(() -> new BusinessException("ROLE_USER not found — seed the roles table first"));
        UserRole assignment = new UserRole();
        assignment.setUser(user);
        assignment.setRole(userRole);
        assignment.setActive(true);
        assignment.setAssignedAt(Instant.now());
        userRoleRepository.save(assignment);

        String token = jwtProvider.generate(user.getUsername());
        return new AuthResponseDTO(token, "Bearer", user.getId(), user.getUsername());
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByUsernameAndDeletedAtIsNull(request.username())
                .orElseThrow(() -> new BusinessException("Invalid username or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BusinessException("Invalid username or password");
        }
        if (!user.isActive()) {
            throw new BusinessException("Account is suspended");
        }

        log.info("Successful login for userId={}", user.getId());
        String token = jwtProvider.generate(user.getUsername());
        return new AuthResponseDTO(token, "Bearer", user.getId(), user.getUsername());
    }
}
