// src/main/java/com/macbul/platform/service/VideoClipService.java
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
public class VideoClipService {

    @Autowired private VideoClipRepository clipRepo;
    @Autowired private MatchVideoRepository mvRepo;
    @Autowired private UserRepository       userRepo;
    @Autowired private MapperUtil           mapper;

    /** Create a new video clip */
    public VideoClipDto createClip(VideoClipCreateRequest req) {
        MatchVideo mv = mvRepo.findById(req.getMatchVideoId())
            .orElseThrow(() -> new ResourceNotFoundException("MatchVideo not found: " + req.getMatchVideoId()));
        User user = userRepo.findById(req.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + req.getUserId()));

        VideoClip vc = new VideoClip();
        vc.setId(UUID.randomUUID().toString());
        vc.setMatchVideo(mv);
        vc.setUser(user);
        vc.setClipUrl(req.getClipUrl());
        vc.setStartSec(req.getStartSec());
        vc.setEndSec(req.getEndSec());
        vc.setCreatedAt(
            req.getCreatedAt() != null 
                ? req.getCreatedAt() 
                : System.currentTimeMillis()
        );

        return mapper.toVideoClipDto(clipRepo.save(vc));
    }

    /** Get one by ID */
    public VideoClipDto getClipById(String id) {
        VideoClip vc = clipRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("VideoClip not found: " + id));
        return mapper.toVideoClipDto(vc);
    }

    /** List all clips */
    public List<VideoClipDto> getAllClips() {
        return clipRepo.findAll().stream()
            .map(mapper::toVideoClipDto)
            .collect(Collectors.toList());
    }

    /** List clips for a given matchVideo */
    public List<VideoClipDto> getByMatchVideoId(String matchVideoId) {
        return clipRepo.findByMatchVideoId(matchVideoId).stream()
            .map(mapper::toVideoClipDto)
            .collect(Collectors.toList());
    }

    /** List clips created by a given user */
    public List<VideoClipDto> getByUserId(String userId) {
        return clipRepo.findByUserId(userId).stream()
            .map(mapper::toVideoClipDto)
            .collect(Collectors.toList());
    }

    /** Update clip URL or time bounds */
    public VideoClipDto updateClip(String id, VideoClipUpdateRequest req) {
        VideoClip vc = clipRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("VideoClip not found: " + id));

        if (req.getClipUrl()  != null) vc.setClipUrl(req.getClipUrl());
        if (req.getStartSec() != null) vc.setStartSec(req.getStartSec());
        if (req.getEndSec()   != null) vc.setEndSec(req.getEndSec());

        return mapper.toVideoClipDto(clipRepo.save(vc));
    }

    /** Delete a clip */
    public void deleteClip(String id) {
        if (!clipRepo.existsById(id)) {
            throw new ResourceNotFoundException("VideoClip not found: " + id);
        }
        clipRepo.deleteById(id);
    }
}
