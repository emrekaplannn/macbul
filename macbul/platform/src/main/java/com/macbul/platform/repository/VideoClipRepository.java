// src/main/java/com/macbul/platform/repository/VideoClipRepository.java
package com.macbul.platform.repository;

import com.macbul.platform.model.VideoClip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * CRUD + list-by-matchVideo and list-by-user for VideoClip.
 */
public interface VideoClipRepository extends JpaRepository<VideoClip, String> {

    List<VideoClip> findByMatchVideoId(String matchVideoId);

    List<VideoClip> findByUserId(String userId);
}
