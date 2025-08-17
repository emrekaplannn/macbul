// src/main/java/com/macbul/platform/service/ReferralService.java
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
import java.util.stream.Collectors;

@Service
@Transactional
public class ReferralService {

    @Autowired private ReferralRepository referralRepo;
    @Autowired private UserRepository     userRepo;
    @Autowired private MatchRepository    matchRepo;
    @Autowired private MapperUtil         mapper;

    /** Create a new referral record */
    public ReferralDto createReferral(ReferralCreateRequest req) {
        User referrer = userRepo.findById(req.getReferrerUserId())
            .orElseThrow(() -> new ResourceNotFoundException("Referrer not found: " + req.getReferrerUserId()));
        User referred = userRepo.findById(req.getReferredUserId())
            .orElseThrow(() -> new ResourceNotFoundException("Referred user not found: " + req.getReferredUserId()));

        Referral ref = new Referral();
        ref.setId(UUID.randomUUID().toString());
        ref.setReferrerUser(referrer);
        ref.setReferredUser(referred);

        if (req.getMatchId() != null) {
            Match match = matchRepo.findById(req.getMatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Match not found: " + req.getMatchId()));
            ref.setMatch(match);
        }

        ref.setRewardAmount(req.getRewardAmount());
        ref.setRewarded(req.getRewarded() != null ? req.getRewarded() : Boolean.FALSE);
        ref.setCreatedAt(req.getCreatedAt() != null ? req.getCreatedAt() : System.currentTimeMillis());

        return mapper.toReferralDto(referralRepo.save(ref));
    }

    /** Get one by ID */
    public ReferralDto getById(String id) {
        Referral ref = referralRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Referral not found: " + id));
        return mapper.toReferralDto(ref);
    }

    /** List all */
    public List<ReferralDto> getAll() {
        return referralRepo.findAll()
            .stream()
            .map(mapper::toReferralDto)
            .collect(Collectors.toList());
    }

    /** List by referrer */
    public List<ReferralDto> getByReferrer(String referrerUserId) {
        return referralRepo.findByReferrerUserId(referrerUserId)
            .stream()
            .map(mapper::toReferralDto)
            .collect(Collectors.toList());
    }

    /** List by referred */
    public List<ReferralDto> getByReferred(String referredUserId) {
        return referralRepo.findByReferredUserId(referredUserId)
            .stream()
            .map(mapper::toReferralDto)
            .collect(Collectors.toList());
    }

    /** List by match */
    public List<ReferralDto> getByMatch(String matchId) {
        return referralRepo.findByMatchId(matchId)
            .stream()
            .map(mapper::toReferralDto)
            .collect(Collectors.toList());
    }

    /** Update reward amount and/or rewarded flag */
    public ReferralDto updateReferral(String id, ReferralUpdateRequest req) {
        Referral ref = referralRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Referral not found: " + id));

        if (req.getRewardAmount() != null) {
            ref.setRewardAmount(req.getRewardAmount());
        }
        if (req.getRewarded() != null) {
            ref.setRewarded(req.getRewarded());
        }

        return mapper.toReferralDto(referralRepo.save(ref));
    }

    /** Delete */
    public void deleteReferral(String id) {
        if (!referralRepo.existsById(id)) {
            throw new ResourceNotFoundException("Referral not found: " + id);
        }
        referralRepo.deleteById(id);
    }
}
