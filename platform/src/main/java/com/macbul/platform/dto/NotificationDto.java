// src/main/java/com/macbul/platform/dto/NotificationDto.java
package com.macbul.platform.dto;

import com.macbul.platform.util.NotificationType;
import lombok.Data;

/**
 * DTO returned to clients for a Notification.
 */
@Data
public class NotificationDto {
    private String id;
    private String userId;
    private NotificationType type;
    private String payload;
    private Boolean isRead;
    private Long createdAt;
}
