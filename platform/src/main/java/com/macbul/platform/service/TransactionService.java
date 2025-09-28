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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.*;

@Service
@Transactional
public class TransactionService {

    @Autowired private TransactionRepository txRepo;
    @Autowired private UserRepository   userRepo;
    @Autowired private MapperUtil       mapper;

    @Autowired private WalletRepository walletRepo;


    @Transactional
    public TransactionDto createTransaction(TransactionCreateRequest req) {
        // 0) Validasyon
        if (req.getType() == null) {
            throw new IllegalArgumentException("Transaction type is required");
        }
        if (req.getAmount() == null || req.getAmount().signum() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        User user = userRepo.findById(req.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + req.getUserId()));

        // 1) Tutarı normalize et (2 ondalık)
        BigDecimal amt = req.getAmount().setScale(2, RoundingMode.HALF_UP);

        // 2) Delta hesapla
        BigDecimal delta = switch (req.getType()) {
            case LOAD   -> amt;              // +amount
            case REFUND -> amt;              // +amount
            case PAY    -> amt.negate();     // -amount
        };

        long now = System.currentTimeMillis();

        // 3) Cüzdanı kilitle (yoksa oluştur)
        Wallet wallet = walletRepo.lockByUserId(user.getId())
            .orElseGet(() -> {
                Wallet w = new Wallet();
                w.setId(UUID.randomUUID().toString());
                w.setUser(user);
                w.setBalance(new BigDecimal("0.00"));
                w.setUpdatedAt(now);
                return w;
            });

        // 4) Yeni bakiye & yeterli bakiye kontrolü
        BigDecimal newBalance = wallet.getBalance().add(delta).setScale(2, RoundingMode.HALF_UP);
        if (newBalance.signum() < 0) {
            throw new IllegalStateException("Insufficient funds");
        }
        wallet.setBalance(newBalance);
        wallet.setUpdatedAt(now);
        walletRepo.save(wallet);

        // 5) Transaction kaydı
        Transaction tx = new Transaction();
        tx.setId(UUID.randomUUID().toString());
        tx.setUser(user);
        tx.setAmount(amt);
        tx.setType(req.getType());
        tx.setDescription(req.getDescription());
        tx.setCreatedAt(req.getCreatedAt() != null ? req.getCreatedAt() : now);

        Transaction saved = txRepo.save(tx);
        return mapper.toTransactionDto(saved);
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
