// src/main/java/com/macbul/platform/service/NotificationService.java
package com.macbul.platform.service;

import com.macbul.platform.dto.*;
import com.macbul.platform.exception.ResourceNotFoundException;
import com.macbul.platform.model.*;
import com.macbul.platform.repository.*;
import com.macbul.platform.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.*;

@Service
@Transactional
public class NotificationService {

    @Autowired private NotificationRepository repo;
    @Autowired private UserRepository          userRepo;
    @Autowired private MapperUtil              mapper;

    /** Create */
    public NotificationDto createNotification(NotificationCreateRequest req) {
        User user = userRepo.findById(req.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + req.getUserId()));

        Notification n = new Notification();
        n.setId(UUID.randomUUID().toString());
        n.setUser(user);
        n.setType(req.getType());
        n.setPayload(req.getPayload());
        n.setCreatedAt(
            req.getCreatedAt() != null
                ? req.getCreatedAt()
                : System.currentTimeMillis()
        );

        return mapper.toNotificationDto(repo.save(n));
    }

    /** Read one */
    public NotificationDto getById(String id) {
        Notification n = repo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Notification not found: " + id));
        return mapper.toNotificationDto(n);
    }

    /** List all */
    public List<NotificationDto> getAll() {
        return repo.findAll().stream()
            .map(mapper::toNotificationDto)
            .collect(Collectors.toList());
    }

    /** List by user */
    public List<NotificationDto> getByUserId(String userId) {
        return repo.findByUserId(userId).stream()
            .map(mapper::toNotificationDto)
            .collect(Collectors.toList());
    }

    /** Update read status */
    public NotificationDto updateNotification(String id, NotificationUpdateRequest req) {
        Notification n = repo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Notification not found: " + id));

        if (req.getIsRead() != null) {
            n.setIsRead(req.getIsRead());
        }
        return mapper.toNotificationDto(repo.save(n));
    }

    /** Delete */
    public void deleteNotification(String id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Notification not found: " + id);
        }
        repo.deleteById(id);
    }
}
