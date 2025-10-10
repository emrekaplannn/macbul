// src/main/java/com/macbul/platform/dto/MatchListItemDto.java
package com.macbul.platform.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchListItemDto {
    private String id;
    private String fieldName;
    private String city;
    private Long matchTimestamp;
    private BigDecimal pricePerUser;
    private Integer totalSlots;

    /** Ödeme yapmış katılımcı sayısı (hasPaid = true) */
    private Integer filledSlots;

    /** Oturumdaki kullanıcı bu maça katıldı mı? */
    private Boolean isUserJoined;
}
