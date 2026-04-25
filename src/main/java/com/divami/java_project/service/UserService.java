package com.divami.java_project.service;

import com.divami.java_project.model.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

/** Manages platform users — listing, retrieval, and profile updates. */
public interface UserService {

    /** Returns a paginated list of all non-deleted users. Admin only. */
    Page<UserDTO> findAll(Pageable pageable);

    /** Returns a single user by ID. Throws ResourceNotFoundException if absent. */
    UserDTO findById(UUID id);

    /** Updates a user's own profile fields (fullName, avatarUrl). Password changes are out of scope here. */
    UserDTO updateProfile(UUID id, UserDTO dto);

    /** Soft-deletes a user account. Admin only. */
    void delete(UUID id);
}
