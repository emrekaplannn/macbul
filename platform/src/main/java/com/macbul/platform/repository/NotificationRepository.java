// src/main/java/com/macbul/platform/repository/NotificationRepository.java
package com.macbul.platform.repository;

import com.macbul.platform.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * CRUD + list-by-user for Notification.
 */
public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findByUserId(String userId);
}
