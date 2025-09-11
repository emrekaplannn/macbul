// src/main/java/com/macbul/platform/service/TransactionService.java
package com.macbul.platform.service;

import com.macbul.platform.dto.*;
import com.macbul.platform.exception.ResourceNotFoundException;
import com.macbul.platform.model.*;
import com.macbul.platform.repository.*;
import com.macbul.platform.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.*;

@Service
@Transactional
public class TransactionService {

    @Autowired private TransactionRepository txRepo;
    @Autowired private UserRepository   userRepo;
    @Autowired private MapperUtil       mapper;

    /** Create */
    public TransactionDto createTransaction(TransactionCreateRequest req) {
        User user = userRepo.findById(req.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + req.getUserId()));

        Transaction tx = new Transaction();
        tx.setId(UUID.randomUUID().toString());
        tx.setUser(user);
        tx.setAmount(req.getAmount());
        tx.setType(req.getType());
        tx.setDescription(req.getDescription());
        tx.setCreatedAt(
            req.getCreatedAt() != null 
                ? req.getCreatedAt()
                : System.currentTimeMillis()
        );

        return mapper.toTransactionDto(txRepo.save(tx));
    }

    /** Read one */
    public TransactionDto getById(String id) {
        Transaction tx = txRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + id));
        return mapper.toTransactionDto(tx);
    }

    /** List all */
    public List<TransactionDto> getAll() {
        return txRepo.findAll()
                     .stream()
                     .map(mapper::toTransactionDto)
                     .collect(Collectors.toList());
    }

    /** List by user */
    public List<TransactionDto> getByUserId(String userId) {
        return txRepo.findByUserIdOrderByCreatedAtDesc(userId)
                     .stream()
                     .map(mapper::toTransactionDto)
                     .collect(Collectors.toList());
    }

    /** Update */
    public TransactionDto updateTransaction(String id, TransactionUpdateRequest req) {
        Transaction tx = txRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + id));

        if (req.getAmount()      != null) tx.setAmount(req.getAmount());
        if (req.getType()        != null) tx.setType(req.getType());
        if (req.getDescription() != null) tx.setDescription(req.getDescription());
        tx.setCreatedAt(
            req.getCreatedAt() != null 
                ? req.getCreatedAt() 
                : tx.getCreatedAt()
        );

        return mapper.toTransactionDto(txRepo.save(tx));
    }

    /** Delete */
    public void deleteById(String id) {
        if (!txRepo.existsById(id)) {
            throw new ResourceNotFoundException("Transaction not found: " + id);
        }
        txRepo.deleteById(id);
    }
}
