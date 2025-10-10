// src/main/java/com/macbul/platform/dto/MatchListFilterRequest.java
package com.macbul.platform.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchListFilterRequest {
    /** Bu tarihten (dahil) sonraki maçlar. Epoch millis. Null ise "şu andan sonrası". */
    private Long fromTimestamp;
}
