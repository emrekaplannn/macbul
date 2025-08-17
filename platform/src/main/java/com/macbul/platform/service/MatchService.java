// src/main/java/com/macbul/platform/service/MatchService.java
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
public class MatchService {

    @Autowired private MatchRepository matchRepo;
    @Autowired private UserRepository  userRepo;
    @Autowired private MapperUtil      mapper;

    /** Create */
    public MatchDto createMatch(MatchCreateRequest req) {
        User organizer = userRepo.findById(req.getOrganizerId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + req.getOrganizerId()));

        Match match = new Match();
        match.setId(UUID.randomUUID().toString());
        match.setOrganizer(organizer);
        match.setFieldName(req.getFieldName());
        match.setAddress(req.getAddress());
        match.setCity(req.getCity());
        match.setMatchTimestamp(req.getMatchTimestamp());
        match.setPricePerUser(req.getPricePerUser());
        match.setTotalSlots(
            req.getTotalSlots() != null
                ? req.getTotalSlots()
                : 14
        );
        match.setCreatedAt(System.currentTimeMillis());

        return mapper.toMatchDto(matchRepo.save(match));
    }

    /** Read one */
    public MatchDto getMatchById(String id) {
        Match match = matchRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Match not found: " + id));
        return mapper.toMatchDto(match);
    }

    /** List all */
    public List<MatchDto> getAllMatches() {
        return matchRepo.findAll()
            .stream()
            .map(mapper::toMatchDto)
            .collect(Collectors.toList());
    }

    /** List by organizer */
    public List<MatchDto> getMatchesByOrganizer(String organizerId) {
        return matchRepo.findByOrganizerId(organizerId)
            .stream()
            .map(mapper::toMatchDto)
            .collect(Collectors.toList());
    }

    /** Update */
    public MatchDto updateMatch(String id, MatchUpdateRequest req) {
        Match match = matchRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Match not found: " + id));

        if (req.getFieldName()      != null) match.setFieldName(req.getFieldName());
        if (req.getAddress()        != null) match.setAddress(req.getAddress());
        if (req.getCity()           != null) match.setCity(req.getCity());
        if (req.getMatchTimestamp() != null) match.setMatchTimestamp(req.getMatchTimestamp());
        if (req.getPricePerUser()   != null) match.setPricePerUser(req.getPricePerUser());
        if (req.getTotalSlots()     != null) match.setTotalSlots(req.getTotalSlots());

        return mapper.toMatchDto(matchRepo.save(match));
    }

    /** Delete */
    public void deleteMatch(String id) {
        if (!matchRepo.existsById(id)) {
            throw new ResourceNotFoundException("Match not found: " + id);
        }
        matchRepo.deleteById(id);
    }
}
