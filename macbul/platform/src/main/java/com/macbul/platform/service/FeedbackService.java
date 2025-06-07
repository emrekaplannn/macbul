// src/main/java/com/macbul/platform/service/FeedbackService.java
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
public class FeedbackService {

    @Autowired private FeedbackRepository feedbackRepo;
    @Autowired private MatchRepository    matchRepo;
    @Autowired private UserRepository     userRepo;
    @Autowired private MapperUtil         mapper;

    /** Create new feedback */
    public FeedbackDto createFeedback(FeedbackCreateRequest req) {
        Match match = matchRepo.findById(req.getMatchId())
            .orElseThrow(() -> new ResourceNotFoundException("Match not found: " + req.getMatchId()));
        User user = userRepo.findById(req.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + req.getUserId()));

        Feedback fb = new Feedback();
        fb.setId(UUID.randomUUID().toString());
        fb.setMatch(match);
        fb.setUser(user);
        fb.setRating(req.getRating());
        fb.setComment(req.getComment());
        fb.setCreatedAt(
            req.getCreatedAt() != null 
                ? req.getCreatedAt() 
                : System.currentTimeMillis()
        );

        return mapper.toFeedbackDto(feedbackRepo.save(fb));
    }

    /** Get feedback by ID */
    public FeedbackDto getFeedbackById(String id) {
        Feedback fb = feedbackRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Feedback not found: " + id));
        return mapper.toFeedbackDto(fb);
    }

    /** List all feedback */
    public List<FeedbackDto> getAllFeedback() {
        return feedbackRepo.findAll()
            .stream()
            .map(mapper::toFeedbackDto)
            .collect(Collectors.toList());
    }

    /** List feedback for a specific match */
    public List<FeedbackDto> getFeedbackByMatchId(String matchId) {
        return feedbackRepo.findByMatchId(matchId)
            .stream()
            .map(mapper::toFeedbackDto)
            .collect(Collectors.toList());
    }

    /** List feedback by a specific user */
    public List<FeedbackDto> getFeedbackByUserId(String userId) {
        return feedbackRepo.findByUserId(userId)
            .stream()
            .map(mapper::toFeedbackDto)
            .collect(Collectors.toList());
    }

    /** Update rating and/or comment */
    public FeedbackDto updateFeedback(String id, FeedbackUpdateRequest req) {
        Feedback fb = feedbackRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Feedback not found: " + id));

        if (req.getRating()  != null) fb.setRating(req.getRating());
        if (req.getComment() != null) fb.setComment(req.getComment());

        return mapper.toFeedbackDto(feedbackRepo.save(fb));
    }

    /** Delete feedback */
    public void deleteFeedback(String id) {
        if (!feedbackRepo.existsById(id)) {
            throw new ResourceNotFoundException("Feedback not found: " + id);
        }
        feedbackRepo.deleteById(id);
    }
}
