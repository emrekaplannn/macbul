// src/main/java/com/macbul/platform/controller/TransactionController.java
package com.macbul.platform.controller;

import com.macbul.platform.dto.*;
import com.macbul.platform.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Transaction API", description = "Create, read, update, delete, list transactions")
@RestController
@RequestMapping("/v1/transactions")
public class TransactionController {

    @Autowired private TransactionService txService;

    @Autowired
    private com.macbul.platform.util.SecurityUtils securityUtils;

    @Operation(summary = "Create transaction")
    @PostMapping
    public ResponseEntity<TransactionDto> create(@RequestBody TransactionCreateRequest req) {
        return ResponseEntity.ok(txService.createTransaction(req));
    }

    @Operation(summary = "Get transaction by ID")
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(txService.getById(id));
    }

    @Operation(summary = "List all transactions")
    @GetMapping
    public ResponseEntity<List<TransactionDto>> listAll() {
        return ResponseEntity.ok(txService.getAll());
    }

    @Operation(summary = "List transactions by user ID")
    @GetMapping("/user")
    public ResponseEntity<List<TransactionDto>> listByUser() {
        String userId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(txService.getByUserId(userId));
    }

    @Operation(summary = "Update transaction")
    @PutMapping("/{id}")
    public ResponseEntity<TransactionDto> update(
        @PathVariable String id,
        @RequestBody TransactionUpdateRequest req
    ) {
        return ResponseEntity.ok(txService.updateTransaction(id, req));
    }

    @Operation(summary = "Delete transaction")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        txService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
