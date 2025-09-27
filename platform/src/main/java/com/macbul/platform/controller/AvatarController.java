// src/main/java/com/macbul/platform/controller/AvatarController.java
package com.macbul.platform.controller;

import com.macbul.platform.dto.AvatarResponse;
import com.macbul.platform.service.UserProfileService;
import com.macbul.platform.service.AvatarOrchestrator;
import com.macbul.platform.service.SupabaseStorageService;
import com.macbul.platform.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Avatar API", description = "Kullanıcı profil fotoğrafı yükleme ve görüntüleme")
@RestController
@RequestMapping("/v1/profile/avatar")
public class AvatarController {

    @Autowired
    private AvatarOrchestrator avatarOrchestrator;

    @Autowired
    private SupabaseStorageService storage;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private SecurityUtils securityUtils;

    private int expiresInSec = 2_592_000; // 30 gün

    @Operation(
        summary = "Profil fotoğrafı yükle",
        description = "Kullanıcının mevcut avatarını (varsa) siler, yeni dosyayı avatars bucketına " +
                      "userId/avatar.{ext} olarak yükler ve DB’de avatarPath’i günceller. " +
                      "Dönüşte path ve kısa süreli signed URL verir."
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AvatarResponse> upload(@RequestPart("file") MultipartFile file) throws Exception {
        final String userId = securityUtils.getCurrentUserId();

        final String contentType = file.getContentType() != null ? file.getContentType() : "image/jpeg";
        final String path = avatarOrchestrator.replaceAvatar(userId, file.getBytes(), contentType);
        final String url  = storage.createSignedDownloadUrl(path, expiresInSec); // görüntüleme linki

        return ResponseEntity.ok(new AvatarResponse(path, url));
    }

    @Operation(
        summary = "Mevcut profil fotoğrafını getir",
        description = "DB’de kayıtlı avatarPath üzerinden kısa süreli signed URL üretip döner. " +
                      "Avatar yoksa boş path/url döner."
    )
    @GetMapping
    public ResponseEntity<AvatarResponse> me() {
        final String userId = securityUtils.getCurrentUserId();
        final String path = userProfileService.getAvatarPath(userId);

        if (path == null || path.isBlank()) {
            return ResponseEntity.ok(new AvatarResponse("", ""));
        }

        final String url = storage.createSignedDownloadUrl(path, expiresInSec);
        return ResponseEntity.ok(new AvatarResponse(path, url));
    }
}
