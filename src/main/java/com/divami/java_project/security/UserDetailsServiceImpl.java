package com.divami.java_project.security;

import com.divami.java_project.model.User;
import com.divami.java_project.repository.UserRepository;
import com.divami.java_project.repository.UserRoleRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Bridges the application's {@link User} entity with Spring Security's authentication pipeline.
 * Roles are loaded from the {@code user_roles} join table so that ROLE_ADMIN users
 * receive both ROLE_USER and ROLE_ADMIN authorities.
 */
@Service
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public UserDetailsServiceImpl(UserRepository userRepository,
                                   UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameAndDeletedAtIsNull(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        List<SimpleGrantedAuthority> authorities = userRoleRepository
                .findByUserIdAndDeletedAtIsNull(user.getId())
                .stream()
                .map(ur -> new SimpleGrantedAuthority(ur.getRole().getName()))
                .toList();

        // Every authenticated user implicitly has ROLE_USER
        if (authorities.stream().noneMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            authorities = new java.util.ArrayList<>(authorities);
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(authorities)
                .disabled(!user.isActive())
                .build();
    }
}
