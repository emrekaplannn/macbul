package com.macbul.platform.dto;

import com.macbul.platform.util.PlayerPosition;
import lombok.Data;

/**
 * Fields required to create a new UserProfile. 
 * The userId is taken from the authenticated user or route.
 */
@Data
public class UserProfileCreateRequest {

    private String fullName;
    private PlayerPosition position;
    private String avatarPath;
    private String bio;

    /**
     * districtId → districts.id (FK)
     * Eğer kullanıcı sadece şehir seçmişse district_name = NULL olan city-only kaydı gönderilir.
     * Eğer hiçbir konum seçmemişse null gönderilir.
     */
    private Integer districtId;
}
