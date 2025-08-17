// src/main/java/com/macbul/platform/dto/NotificationUpdateRequest.java
package com.macbul.platform.dto;

import lombok.Data;

/**
 * Fields that can be updated on an existing notification.
 */
@Data
public class NotificationUpdateRequest {
    private Boolean isRead;
}
