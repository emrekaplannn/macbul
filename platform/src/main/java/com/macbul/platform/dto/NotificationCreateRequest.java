// src/main/java/com/macbul/platform/dto/NotificationCreateRequest.java
package com.macbul.platform.dto;

import com.macbul.platform.util.NotificationType;
import lombok.Data;

/**
 * Fields required to create a new notification.
 */
@Data
public class NotificationCreateRequest {
    private String userId;
    private NotificationType type;
    private String payload;    // optional
    private Long createdAt;    // optional
}
