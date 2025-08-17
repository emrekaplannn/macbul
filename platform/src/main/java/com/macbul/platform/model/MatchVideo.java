// src/main/java/com/macbul/platform/model/MatchVideo.java
package com.macbul.platform.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * JPA entity for a video uploaded to a match.
 */
@Entity
@Table(
    name = "match_videos",
    indexes = {
        @Index(name = "idx_match_videos_match", columnList = "match_id")
    }
)
@Data
public class MatchVideo {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @Column(name = "video_url", length = 255, nullable = false)
    private String videoUrl;

    @Column(name = "uploaded_at", nullable = false)
    private Long uploadedAt;
}
