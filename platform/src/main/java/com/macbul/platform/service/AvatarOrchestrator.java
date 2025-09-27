// src/main/java/com/macbul/platform/service/AvatarOrchestrator.java
package com.macbul.platform.service;

import org.springframework.stereotype.Service;

@Service
public class AvatarOrchestrator {

  private final SupabaseStorageService storage;
  private final UserProfileService profiles;

  public AvatarOrchestrator(SupabaseStorageService storage, UserProfileService profiles) {
    this.storage = storage;
    this.profiles = profiles;
  }

  /** Eski avatar(lar)ı sil, sabit isimle yenisini yükle, DB’de path’i güncelle ve path döndür. */
  public String replaceAvatar(String userId, byte[] bytes, String contentType) {
    // 1) hepsini sil
    String prefix = userId + "/";
    storage.deleteAllUnderPrefix(prefix);

    // 2) uzantı
    String ct = (contentType != null ? contentType.toLowerCase() : "image/jpeg");
    String ext = ct.contains("png") ? "png" : ct.contains("webp") ? "webp" : "jpg";

    // 3) sabit isim
    String path = userId + "/avatar." + ext;

    // 4) yükle
    storage.uploadDirect(path, bytes, ct);

    // 5) DB güncelle
    profiles.updateAvatarPath(userId, path);

    return path;
  }
}
