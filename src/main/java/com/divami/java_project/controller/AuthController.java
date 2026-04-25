package com.divami.java_project.controller;

import com.divami.java_project.model.dto.AuthResponseDTO;
import com.divami.java_project.model.dto.LoginRequestDTO;
import com.divami.java_project.model.dto.RegisterRequestDTO;
import com.divami.java_project.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles user registration and JWT-based authentication.
 * All endpoints are publicly accessible — no authentication required.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Registers a new user account.
     * Returns 201 with a JWT token so the user can immediately start making requests.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        log.info("Registration request for username={}", request.username());
        AuthResponseDTO response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Authenticates with username + password and returns a JWT access token.
     * Returns 200 on success; GlobalExceptionHandler converts auth failures to 401.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        log.info("Login attempt for username={}", request.username());
        return ResponseEntity.ok(authService.login(request));
    }
}
