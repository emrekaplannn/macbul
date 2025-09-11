// src/main/java/com/macbul/platform/service/ReferralCodeService.java
package com.macbul.platform.service;

import com.macbul.platform.dto.*;
import com.macbul.platform.exception.ResourceNotFoundException;
import com.macbul.platform.model.*;
import com.macbul.platform.repository.*;
import com.macbul.platform.util.MapperUtil;
import com.macbul.platform.util.ReferralCodeStatus;
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReferralCodeService {

    @Autowired private ReferralCodeRepository repo;
    @Autowired private UserRepository         userRepo;
    @Autowired private MapperUtil             mapper;

    
    // --- Random code generator state ---
    private static final String RC_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RC_RNG = new SecureRandom();

    /** Create a new referral code */
    public ReferralCodeDto create(ReferralCodeCreateRequest req, String userId) {
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        // enforce code uniqueness
        repo.findByCode(req.getCode()).ifPresent(rc -> {
            throw new IllegalArgumentException("Code already in use: " + req.getCode());
        });

        // Deactivate existing active codes for this user
        List<ReferralCode> actives = repo
                .findByUserIdAndStatus(user.getId(), ReferralCodeStatus.ACTIVE);
        actives.forEach(rc -> rc.setStatus(ReferralCodeStatus.INACTIVE));
        repo.saveAll(actives);

        ReferralCode rc = new ReferralCode();
        rc.setId(UUID.randomUUID().toString());
        rc.setUser(user);
        rc.setCode(req.getCode());
        rc.setStatus(ReferralCodeStatus.ACTIVE);
        rc.setCreatedAt(System.currentTimeMillis());

        return mapper.toReferralCodeDto(repo.save(rc));
    }

    /** Get by ID */
    public ReferralCodeDto getById(String id) {
        ReferralCode rc = repo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ReferralCode not found: " + id));
        return mapper.toReferralCodeDto(rc);
    }

    public ReferralCodeDto getActiveCodeForUser(String userId) {
        ReferralCode rc = repo
                .findFirstByUserIdAndStatusOrderByCreatedAtDesc(userId, ReferralCodeStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Active referral code not found for user " + userId));
        return mapper.toReferralCodeDto(rc);
    }

    /** List all */
    public List<ReferralCodeDto> getAll() {
        return repo.findAll()
            .stream()
            .map(mapper::toReferralCodeDto)
            .collect(Collectors.toList());
    }

    /** List by user */
    public List<ReferralCodeDto> getByUserId(String userId) {
        return repo.findByUserId(userId)
            .stream()
            .map(mapper::toReferralCodeDto)
            .collect(Collectors.toList());
    }

    /** Lookup by code */
    public ReferralCodeDto getByCode(String code) {
        ReferralCode rc = repo.findByCode(code)
            .orElseThrow(() -> new ResourceNotFoundException("ReferralCode not found: " + code));
        return mapper.toReferralCodeDto(rc);
    }

    /** Update code value */
    public ReferralCodeDto update(String id, ReferralCodeUpdateRequest req) {
        ReferralCode rc = repo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ReferralCode not found: " + id));

        if (req.getCode() != null && !req.getCode().equals(rc.getCode())) {
            // enforce uniqueness
            repo.findByCode(req.getCode()).ifPresent(existing -> {
                throw new IllegalArgumentException("Code already in use: " + req.getCode());
            });
            rc.setCode(req.getCode());
        }

        rc.setStatus(req.getStatus() != null ? req.getStatus() : rc.getStatus());

        return mapper.toReferralCodeDto(repo.save(rc));
    }

    /** Delete */
    public void delete(String id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("ReferralCode not found: " + id);
        }
        repo.deleteById(id);
    }

    /** Create a new ACTIVE referral code for the given user with a random 8-char code. */
    public ReferralCodeDto createRandomForUser(String userId) {
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        // Deactivate existing ACTIVE codes for this user
        List<ReferralCode> actives =
                repo.findByUserIdAndStatus(user.getId(), ReferralCodeStatus.ACTIVE);
        actives.forEach(rc -> rc.setStatus(ReferralCodeStatus.INACTIVE));
        repo.saveAll(actives);

        // Generate a unique 8-char uppercase alphanumeric code
        String code = generateUniqueCode(8);

        ReferralCode rc = new ReferralCode();
        rc.setId(UUID.randomUUID().toString());
        rc.setUser(user);
        rc.setCode(code);
        rc.setStatus(ReferralCodeStatus.ACTIVE);
        rc.setCreatedAt(System.currentTimeMillis());

        return mapper.toReferralCodeDto(repo.save(rc));
    }

    // --- helpers ---
    private String generateUniqueCode(int len) {
        String code;
        // retry until unique (fast due to large space: 36^8)
        do {
            code = randomCode(len);
        } while (repo.findByCode(code).isPresent());
        return code;
    }

    private String randomCode(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(RC_ALPHABET.charAt(RC_RNG.nextInt(RC_ALPHABET.length())));
        }
        return sb.toString();
    }
}
