// src/main/java/com/macbul/platform/service/WalletService.java
package com.macbul.platform.service;

import com.macbul.platform.dto.*;
import com.macbul.platform.exception.ResourceNotFoundException;
import com.macbul.platform.model.User;
import com.macbul.platform.model.Wallet;
import com.macbul.platform.repository.UserRepository;
import com.macbul.platform.repository.WalletRepository;
import com.macbul.platform.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Business logic for creating, reading, updating, and deleting wallets.
 */
@Service
@Transactional
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MapperUtil mapperUtil;

    /**
     * Create a new Wallet for an existing User.
     * If initialBalance is null, defaults to 0.00.
     */
    public WalletDto createWallet(WalletCreateRequest request, String userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        if (walletRepository.findByUserId(user.getId()).isPresent()) {
            throw new IllegalArgumentException("Wallet already exists for user: " + user.getId());
        }

        Wallet wallet = new Wallet();
        wallet.setId(UUID.randomUUID().toString());
        wallet.setUser(user);
        wallet.setBalance(
            request.getInitialBalance() != null
                ? request.getInitialBalance()
                : BigDecimal.ZERO
        );
        wallet.setUpdatedAt(System.currentTimeMillis());

        Wallet saved = walletRepository.save(wallet);
        return mapperUtil.toWalletDto(saved);
    }


    /**
     * Retrieve a Wallet by its ID.
     */
    public WalletDto getWalletById(String walletId) {
        Wallet wallet = walletRepository.findById(walletId)
            .orElseThrow(() -> new ResourceNotFoundException("Wallet not found: " + walletId));
        return mapperUtil.toWalletDto(wallet);
    }

    /**
     * Retrieve a Wallet by the associated User’s ID.
     */
    public WalletDto getWalletByUserId(String userId) {
        Wallet wallet = walletRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user: " + userId));
        return mapperUtil.toWalletDto(wallet);
    }

    /**
     * List all Wallets.
     */
    public List<WalletDto> getAllWallets() {
        return walletRepository.findAll()
            .stream()
            .map(mapperUtil::toWalletDto)
            .collect(Collectors.toList());
    }

    /**
     * Update an existing Wallet’s balance and/or updatedAt by walletId.
     * Only non-null fields in request are applied.
     */
    public WalletDto updateWallet(String walletId, WalletUpdateRequest request) {
        Wallet existing = walletRepository.findById(walletId)
            .orElseThrow(() -> new ResourceNotFoundException("Wallet not found: " + walletId));

        if (request.getBalance() != null) {
            existing.setBalance(request.getBalance());
        }
        if (request.getUpdatedAt() != null) {
            existing.setUpdatedAt(request.getUpdatedAt());
        } else {
            existing.setUpdatedAt(System.currentTimeMillis());
        }

        Wallet updated = walletRepository.save(existing);
        return mapperUtil.toWalletDto(updated);
    }

    /**
     * Delete a Wallet by its ID.
     */
    public void deleteWallet(String walletId) {
        if (!walletRepository.existsById(walletId)) {
            throw new ResourceNotFoundException("Wallet not found: " + walletId);
        }
        walletRepository.deleteById(walletId);
    }
}
