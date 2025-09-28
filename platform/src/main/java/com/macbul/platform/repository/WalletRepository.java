// src/main/java/com/macbul/platform/repository/WalletRepository.java
package com.macbul.platform.repository;

import com.macbul.platform.model.Wallet;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select w from Wallet w where w.user.id = :userId")
    Optional<Wallet> lockByUserId(@Param("userId") String userId);
}
