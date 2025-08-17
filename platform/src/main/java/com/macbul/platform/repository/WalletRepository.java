// src/main/java/com/macbul/platform/repository/WalletRepository.java
package com.macbul.platform.repository;

import com.macbul.platform.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for basic CRUD on Wallet (ID type = String).
 * Also provides a finder by user ID.
 */
public interface WalletRepository extends JpaRepository<Wallet, String> {

    /**
     * Find the Wallet belonging to a given user (user.id).
     */
    Optional<Wallet> findByUserId(String userId);
}
