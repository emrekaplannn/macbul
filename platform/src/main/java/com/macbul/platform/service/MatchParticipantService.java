// src/main/java/com/macbul/platform/service/MatchParticipantService.java
package com.macbul.platform.service;

import com.macbul.platform.dto.*;
import com.macbul.platform.exception.ResourceNotFoundException;
import com.macbul.platform.model.*;
import com.macbul.platform.repository.*;
import com.macbul.platform.util.MapperUtil;
import com.macbul.platform.util.TransactionType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class MatchParticipantService {

    @Autowired private MatchParticipantRepository mpRepo;
    @Autowired private MatchRepository            matchRepo;
    @Autowired private UserRepository             userRepo;
    @Autowired private TeamRepository             teamRepo;
    @Autowired private MapperUtil                 mapper;
    @Autowired
    private WalletService walletService;

    @Autowired
    private TransactionService transactionService;

    /** Create a new participation record */
    public MatchParticipantDto create(MatchParticipantCreateRequest req) {
        // verify match & user
        Match match = matchRepo.findById(req.getMatchId())
            .orElseThrow(() -> new ResourceNotFoundException("Match not found: " + req.getMatchId()));
        User user = userRepo.findById(req.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + req.getUserId()));

        long now = System.currentTimeMillis(); 

        // ✅ önce dolu slotları kontrol et
        long paidCount = mpRepo.countByMatchIdAndHasPaidTrue(match.getId());
        if (paidCount >= match.getTotalSlots()) {
            throw new IllegalStateException(
                "Match " + match.getId() + " is already full (" +
                paidCount + "/" + match.getTotalSlots() + ")"
            );
        }
        
        // enforce one participation per user/match
        if (mpRepo.existsByMatchIdAndUserId(match.getId(), user.getId())) {
            throw new IllegalArgumentException(
                "User " + user.getId() + " already joined match " + match.getId()
            );
        }

        // check wallet balance
        WalletDto walletDto = walletService.getWalletByUserId(req.getUserId());
        if (walletDto.getBalance().compareTo(match.getPricePerUser()) < 0) {
            throw new IllegalArgumentException(
                "Insufficient balance in wallet for user " + user.getId()
            );
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        java.time.LocalDateTime matchDateTime = java.time.Instant.ofEpochMilli(match.getMatchTimestamp())
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDateTime();
        String formatted = formatter.format(matchDateTime);
        System.out.println("Formatted date: " + formatted);

        TransactionCreateRequest transactionRequest = new TransactionCreateRequest();
        transactionRequest.setUserId(req.getUserId());
        transactionRequest.setAmount(match.getPricePerUser());
        transactionRequest.setType(TransactionType.PAY);
        transactionRequest.setDescription("Maç ödemesi - " + match.getFieldName() + " / " + formatted);
        transactionRequest.setCreatedAt(now);
        transactionService.createTransaction(transactionRequest);

        MatchParticipant mp = new MatchParticipant();
        mp.setId(UUID.randomUUID().toString());
        mp.setMatch(match);
        mp.setUser(user);

        // optional team
        if (req.getTeamId() != null) {
            Team team = teamRepo.findById(req.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found: " + req.getTeamId()));
            mp.setTeam(team);
        }

        mp.setJoinedAt(
            req.getJoinedAt() != null 
                ? req.getJoinedAt() 
                : System.currentTimeMillis()
        );
        mp.setHasPaid(
            req.getHasPaid() != null 
                ? req.getHasPaid() 
                : Boolean.FALSE
        );

        return mapper.toMatchParticipantDto(mpRepo.save(mp));
    }

    /** Get by ID */
    public MatchParticipantDto getById(String id) {
        MatchParticipant mp = mpRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Participation not found: " + id));
        return mapper.toMatchParticipantDto(mp);
    }

    /** List all */
    public List<MatchParticipantDto> getAll() {
        return mpRepo.findAll()
            .stream()
            .map(mapper::toMatchParticipantDto)
            .collect(Collectors.toList());
    }

    /** List by match */
    public List<MatchParticipantDto> getByMatchId(String matchId) {
        return mpRepo.findByMatchId(matchId)
            .stream()
            .map(mapper::toMatchParticipantDto)
            .collect(Collectors.toList());
    }

    /** List by user */
    public List<MatchParticipantDto> getByUserId(String userId) {
        return mpRepo.findByUserId(userId)
            .stream()
            .map(mapper::toMatchParticipantDto)
            .collect(Collectors.toList());
    }

    /** Update team assignment or payment status */
    public MatchParticipantDto update(String id, MatchParticipantUpdateRequest req) {
        MatchParticipant mp = mpRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Participation not found: " + id));

        if (req.getTeamId() != null) {
            Team team = teamRepo.findById(req.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found: " + req.getTeamId()));
            mp.setTeam(team);
        }
        if (req.getHasPaid() != null) {
            mp.setHasPaid(req.getHasPaid());
        }

        return mapper.toMatchParticipantDto(mpRepo.save(mp));
    }

    /** Delete */
    public void delete(String id) {
        if (!mpRepo.existsById(id)) {
            throw new ResourceNotFoundException("Participation not found: " + id);
        }
        mpRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public MatchSlotsDto getSlotsStatus(String matchId) {
        Match match = matchRepo.findById(matchId)
            .orElseThrow(() -> new ResourceNotFoundException("Match not found: " + matchId));

        long paidCount = mpRepo.countByMatchIdAndHasPaidTrue(match.getId());
        int total = Optional.ofNullable(match.getTotalSlots()).orElse(0);
        int remaining = Math.max(0, total - (int) paidCount);
        boolean full = paidCount >= total && total > 0;

        return MatchSlotsDto.builder()
                .matchId(match.getId())
                .totalSlots(total)
                .paidCount(paidCount)
                .remaining(remaining)
                .full(full)
                .build();
    }
}
