package com.divami.java_project.service;

import com.divami.java_project.model.dto.AuthResponseDTO;
import com.divami.java_project.model.dto.LoginRequestDTO;
import com.divami.java_project.model.dto.RegisterRequestDTO;

/** Handles user registration and JWT-based authentication. */
public interface AuthService {

    /** Registers a new user and returns an access token. */
    AuthResponseDTO register(RegisterRequestDTO request);

    /** Validates credentials and returns a signed JWT access token. */
    AuthResponseDTO login(LoginRequestDTO request);
}
