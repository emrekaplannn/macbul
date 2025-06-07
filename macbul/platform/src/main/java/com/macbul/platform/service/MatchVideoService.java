// src/main/java/com/macbul/platform/service/MatchVideoService.java
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
public class MatchVideoService {

    @Autowired private MatchVideoRepository videoRepo;
    @Autowired private MatchRepository      matchRepo;
    @Autowired private MapperUtil           mapper;

    /** Create */
    public MatchVideoDto create(MatchVideoCreateRequest req) {
        Match match = matchRepo.findById(req.getMatchId())
            .orElseThrow(() -> new ResourceNotFoundException("Match not found: " + req.getMatchId()));

        MatchVideo mv = new MatchVideo();
        mv.setId(UUID.randomUUID().toString());
        mv.setMatch(match);
        mv.setVideoUrl(req.getVideoUrl());
        mv.setUploadedAt(
            req.getUploadedAt() != null 
                ? req.getUploadedAt() 
                : System.currentTimeMillis()
        );

        return mapper.toMatchVideoDto(videoRepo.save(mv));
    }

    /** Read one */
    public MatchVideoDto getById(String id) {
        MatchVideo mv = videoRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Video not found: " + id));
        return mapper.toMatchVideoDto(mv);
    }

    /** List all */
    public List<MatchVideoDto> getAll() {
        return videoRepo.findAll()
            .stream()
            .map(mapper::toMatchVideoDto)
            .collect(Collectors.toList());
    }

    /** List by match */
    public List<MatchVideoDto> getByMatchId(String matchId) {
        return videoRepo.findByMatchId(matchId)
            .stream()
            .map(mapper::toMatchVideoDto)
            .collect(Collectors.toList());
    }

    /** Update */
    public MatchVideoDto update(String id, MatchVideoUpdateRequest req) {
        MatchVideo mv = videoRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Video not found: " + id));

        if (req.getVideoUrl() != null) {
            mv.setVideoUrl(req.getVideoUrl());
        }

        return mapper.toMatchVideoDto(videoRepo.save(mv));
    }

    /** Delete */
    public void delete(String id) {
        if (!videoRepo.existsById(id)) {
            throw new ResourceNotFoundException("Video not found: " + id);
        }
        videoRepo.deleteById(id);
    }
}
