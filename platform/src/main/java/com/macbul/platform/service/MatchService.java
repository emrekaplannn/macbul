// src/main/java/com/macbul/platform/service/MatchService.java
package com.macbul.platform.service;

import com.macbul.platform.dto.*;
import com.macbul.platform.exception.ResourceNotFoundException;
import com.macbul.platform.model.*;
import com.macbul.platform.repository.*;
import com.macbul.platform.util.City;
import com.macbul.platform.util.MapperUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;     // ✅ logging
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class MatchService {

    private static final Logger log = LoggerFactory.getLogger(MatchService.class);  // ✅ logger

    @Autowired private MatchRepository matchRepo;
    @Autowired private UserRepository  userRepo;
    @Autowired private MapperUtil      mapper;
    @Autowired private MatchParticipantRepository mpRepo;
    @Autowired private DistrictRepository districtRepo;

    /** Create */
    public MatchDto createMatch(MatchCreateRequest req) {
        log.debug("[createMatch] called with organizerId={}, fieldName={}, timestamp={}",
                req.getOrganizerId(), req.getFieldName(), req.getMatchTimestamp());

        // -----------------------------
        // 1) Organizer kontrolü
        // -----------------------------
        User organizer = userRepo.findById(req.getOrganizerId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + req.getOrganizerId()));

        // -----------------------------
        // 2) District resolve (nullable)
        // -----------------------------
        District district = null;

        // --- District ID bul ---
        // Kullanıcının şehir seçmesi ZORUNLU
        if (req.getCity() == null) {
            throw new IllegalArgumentException("Şehir seçilmelidir.");
        }

        // District varsa
        Integer districtId;
        if (req.getDistrictName() != null && !req.getDistrictName().isBlank()) {

            districtId = districtRepo
                    .findIdByCityAndDistrictName(req.getCity(), req.getDistrictName())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Geçersiz ilçe: " + req.getCity() + " / " + req.getDistrictName()));

        } else {
            // Sadece şehir (city-only district) için kayıt bul
            districtId = districtRepo
                    .findIdByCityAndDistrictNameIsNull(req.getCity())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Seçilen şehir için kayıt bulunamadı: " + req.getCity()));
        }
        if (districtId != null) {
            district = districtRepo.findById(districtId)
                    .orElseThrow(() -> new ResourceNotFoundException("District not found: " + districtId));
        }

        // -----------------------------
        // 3) Match oluştur
        // -----------------------------
        Match match = new Match();
        match.setId(UUID.randomUUID().toString());
        match.setOrganizer(organizer);
        match.setFieldName(req.getFieldName());
        match.setAddress(req.getAddress());

        match.setDistrict(district); // ← FK (nullable allowed)

        match.setMatchTimestamp(req.getMatchTimestamp());
        match.setPricePerUser(req.getPricePerUser());
        match.setTotalSlots(req.getTotalSlots() != null ? req.getTotalSlots() : 14);
        match.setCreatedAt(System.currentTimeMillis());

        Match saved = matchRepo.save(match);
        return mapper.toMatchDto(saved);
    }



    /** Read one */
    public MatchDto getMatchById(String id) {
        log.debug("[getMatchById] id={}", id);

        Match match = matchRepo.findById(id)
            .orElseThrow(() -> {
                log.warn("[getMatchById] NOT FOUND: {}", id);
                return new ResourceNotFoundException("Match not found: " + id);
            });

        return mapper.toMatchDto(match);
    }



    /** List all */
    public List<MatchDto> getAllMatches() {
        log.debug("[getAllMatches] retrieving all matches");
        List<Match> matches = matchRepo.findAll();
        log.debug("[getAllMatches] found {} matches", matches.size());
        return matches.stream().map(mapper::toMatchDto).collect(Collectors.toList());
    }


    /** List by organizer */
    public List<MatchDto> getMatchesByOrganizer(String organizerId) {
        log.debug("[getMatchesByOrganizer] organizerId={}", organizerId);
        List<Match> matches = matchRepo.findByOrganizerId(organizerId);
        log.debug("[getMatchesByOrganizer] found {} matches", matches.size());
        return matches.stream().map(mapper::toMatchDto).collect(Collectors.toList());
    }


    /** Update */
    public MatchDto updateMatch(String id, MatchUpdateRequest req) {
        log.debug("[updateMatch] id={}, req={}", id, req);

        Match match = matchRepo.findById(id)
            .orElseThrow(() -> {
                log.warn("[updateMatch] NOT FOUND: {}", id);
                return new ResourceNotFoundException("Match not found: " + id);
            });

        // ------------------------------------
        // 1) Basit alanlar
        // ------------------------------------
        if (req.getFieldName()      != null) match.setFieldName(req.getFieldName());
        if (req.getAddress()        != null) match.setAddress(req.getAddress());
        if (req.getMatchTimestamp() != null) match.setMatchTimestamp(req.getMatchTimestamp());
        if (req.getPricePerUser()   != null) match.setPricePerUser(req.getPricePerUser());
        if (req.getTotalSlots()     != null) match.setTotalSlots(req.getTotalSlots());

        // ------------------------------------
        // 2) City + District güncelleme
        //    (city geldiyse lokasyonu güncelle)
        // ------------------------------------
        if (req.getCity() != null) {
            Integer districtId;

            if (req.getDistrictName() != null && !req.getDistrictName().isBlank()) {
                // Şehir + ilçe birlikte verilmiş
                districtId = districtRepo
                    .findIdByCityAndDistrictName(req.getCity(), req.getDistrictName())
                    .orElseThrow(() ->
                        new IllegalArgumentException("Geçersiz ilçe: " 
                            + req.getCity() + " / " + req.getDistrictName()));
            } else {
                // Sadece şehir verilmiş (city-only district)
                districtId = districtRepo
                    .findIdByCityAndDistrictNameIsNull(req.getCity())
                    .orElseThrow(() ->
                        new IllegalArgumentException("Seçilen şehir için kayıt bulunamadı: " 
                            + req.getCity()));
            }

            // District entity'yi yükle ve maça set et
            District district = districtRepo.findById(districtId)
                .orElseThrow(() ->
                    new IllegalStateException("District not found for id=" + districtId));

            match.setDistrict(district);   // FK
        }

        Match saved = matchRepo.save(match);
        log.debug("[updateMatch] UPDATED id={}", saved.getId());

        return mapper.toMatchDto(saved);
    }


    /** Delete */
    public void deleteMatch(String id) {
        log.debug("[deleteMatch] id={}", id);
        if (!matchRepo.existsById(id)) {
            log.warn("[deleteMatch] NOT FOUND: {}", id);
            throw new ResourceNotFoundException("Match not found: " + id);
        }
        matchRepo.deleteById(id);
        log.debug("[deleteMatch] DELETED id={}", id);
    }


    /** Filtered list */
    public List<MatchListItemDto> listFiltered(MatchListFilterRequest req, String currentUserId) {
        Long fromTs = (req != null && req.getFromTimestamp() != null)
                ? req.getFromTimestamp()
                : System.currentTimeMillis();

        log.debug("[listFiltered] called by userId={}, fromTimestamp={}", currentUserId, fromTs);

        List<Match> matches = matchRepo.findByMatchTimestampGreaterThanEqual(fromTs);
        log.debug("[listFiltered] repo returned {} matches", matches.size());

        if (matches.isEmpty()) {
            log.debug("[listFiltered] no matches found after {}", fromTs);
            return Collections.emptyList();
        }

        List<MatchListItemDto> result = matches.stream().map(m -> {
            int filled = (int) mpRepo.countByMatchIdAndHasPaidTrue(m.getId());
            boolean joined = mpRepo.existsByMatchIdAndUserId(m.getId(), currentUserId);

            // -----------------------------
            // District -> city + districtName
            // -----------------------------
            City city = null;
            String districtName = null;

            District d = m.getDistrict();
            if (d != null) {
                city = d.getCity();              // enum City
                districtName = d.getDistrictName(); // null olabilir (city-only kayıt)
            }

            log.trace("[listFiltered] matchId={}, filled={}, joined={}, city={}, district={}",
                    m.getId(), filled, joined, city, districtName);

            return MatchListItemDto.builder()
                    .id(m.getId())
                    .fieldName(m.getFieldName())
                    .city(city)
                    .districtName(districtName)
                    .matchTimestamp(m.getMatchTimestamp())
                    .pricePerUser(m.getPricePerUser())
                    .totalSlots(m.getTotalSlots())
                    .filledSlots(filled)
                    .isUserJoined(joined)
                    .build();
        }).collect(Collectors.toList());

        log.debug("[listFiltered] returning {} matches to client", result.size());
        return result;
    }

}
