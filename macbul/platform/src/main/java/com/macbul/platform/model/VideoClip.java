// src/main/java/com/macbul/platform/model/VideoClip.java
package com.macbul.platform.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * JPA entity representing a clip extracted from a match video.
 */
@Entity
@Table(
    name = "video_clips",
    indexes = {
        @Index(name = "idx_video_clips_mv",   columnList = "match_video_id"),
        @Index(name = "idx_video_clips_user", columnList = "user_id")
    }
)
@Data
public class VideoClip {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    /** The source match video */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_video_id", nullable = false)
    private MatchVideo matchVideo;

    /** The user who created this clip */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** URL pointing to the clip */
    @Column(name = "clip_url", length = 255, nullable = false)
    private String clipUrl;

    /** Start time in seconds */
    @Column(name = "start_sec", nullable = false)
    private Integer startSec;

    /** End time in seconds */
    @Column(name = "end_sec", nullable = false)
    private Integer endSec;

    /** Epoch millis when this clip was created */
    @Column(name = "created_at", nullable = false)
    private Long createdAt;
}
