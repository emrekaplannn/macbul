// src/main/java/com/macbul/platform/repository/TransactionRepository.java
package com.macbul.platform.repository;

import com.macbul.platform.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * CRUD operations for Transaction.
 */
public interface TransactionRepository 
        extends JpaRepository<Transaction, String> {

    /**
     * List all transactions for a given user.
     */
    List<Transaction> findByUserId(String userId);

    List<Transaction> findByUserIdOrderByCreatedAtDesc(String userId);

}
