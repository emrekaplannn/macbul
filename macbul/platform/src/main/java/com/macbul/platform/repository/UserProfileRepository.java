package com.macbul.platform.repository;

import com.macbul.platform.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for basic CRUD on UserProfile (primary key type = String).
 */
public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
    // All common methods (save, findById, deleteById, findAll, etc.) are inherited.
}
