// src/main/java/com/macbul/platform/controller/WalletController.java
package com.macbul.platform.controller;

import com.macbul.platform.dto.*;
import com.macbul.platform.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST endpoints for managing Wallets.
 */
@Tag(name = "Wallet API", description = "Create, retrieve, update, and delete user wallets")
@RestController
@RequestMapping("/v1/wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Operation(summary = "Create a new wallet", description = "Creates a wallet for an existing user (initial balance optional)")
    @PostMapping
    public ResponseEntity<WalletDto> createWallet(@RequestBody WalletCreateRequest request) {
        WalletDto created = walletService.createWallet(request);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Get wallet by ID", description = "Fetch a wallet by its wallet ID")
    @GetMapping("/{walletId}")
    public ResponseEntity<WalletDto> getWalletById(@PathVariable String walletId) {
        WalletDto dto = walletService.getWalletById(walletId);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Get wallet by user ID", description = "Fetch a wallet by the associated user’s ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<WalletDto> getWalletByUserId(@PathVariable String userId) {
        WalletDto dto = walletService.getWalletByUserId(userId);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "List all wallets", description = "Returns a list of all wallets")
    @GetMapping
    public ResponseEntity<List<WalletDto>> listWallets() {
        List<WalletDto> all = walletService.getAllWallets();
        return ResponseEntity.ok(all);
    }

    @Operation(summary = "Update wallet", description = "Update balance and/or updatedAt for the specified wallet")
    @PutMapping("/{walletId}")
    public ResponseEntity<WalletDto> updateWallet(
            @PathVariable String walletId,
            @RequestBody WalletUpdateRequest request
    ) {
        WalletDto updated = walletService.updateWallet(walletId, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete wallet", description = "Deletes the specified wallet by ID")
    @DeleteMapping("/{walletId}")
    public ResponseEntity<Void> deleteWallet(@PathVariable String walletId) {
        walletService.deleteWallet(walletId);
        return ResponseEntity.noContent().build();
    }


    
}
