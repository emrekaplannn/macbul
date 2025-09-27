// src/main/java/com/macbul/platform/service/OtpService.java
package com.macbul.platform.service;

import com.macbul.platform.dto.OtpCreateRequest;
import com.macbul.platform.dto.OtpCreateResponse;
import com.macbul.platform.dto.OtpDto;
import com.macbul.platform.dto.OtpVerifyRequest;
import com.macbul.platform.dto.OtpVerifyResponse;
import com.macbul.platform.model.Otp;
import com.macbul.platform.model.User;
import com.macbul.platform.repository.OtpRepository;
import com.macbul.platform.repository.UserRepository;
import com.macbul.platform.util.OtpType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@Transactional
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${otp.defaultTtlSec:600}")
    private int defaultTtlSec; // varsayılan 10 dk

    /**
     * Yeni OTP oluşturur.
     * - Aynı user+type için tüm eski kayıtları siler.
     * - destination (email/telefon) zorunlu tutulur.
     */
    public OtpCreateResponse create(String userId, OtpCreateRequest req, boolean returnCode) {
        if (req == null || req.getType() == null) {
            throw new IllegalArgumentException("type is required");
        }
        if (req.getDestination() == null || req.getDestination().isBlank()) {
            throw new IllegalArgumentException("destination is required");
        }

        // Varsa önce hepsini temizle (aktif/pasif fark etmeksizin)
        //otpRepository.deleteByUserIdAndType(userId, req.getType());

        long now = Instant.now().toEpochMilli();
        long expiresAt = now + (defaultTtlSec * 1000L);
        String code = generate6DigitNumericCode(); // 6 haneli numeric

        Otp otp = new Otp();
        otp.setId(UUID.randomUUID().toString());
        otp.setUserId(userId);
        otp.setCode(code);
        otp.setType(req.getType());
        otp.setDestination(req.getDestination());
        otp.setCreatedAt(now);
        otp.setExpiresAt(expiresAt);
        otp.setUsedAt(null);

        otpRepository.save(otp);

        return OtpCreateResponse.builder()
                .otp(OtpDto.fromEntity(otp))
                .code(returnCode ? code : null) // dev/test için geri döndürülebilir
                .build();
    }

    /**
     * Aktif (kullanılmamış ve süresi geçmemiş) en güncel OTP’yi döndürür.
     */
    @Transactional(readOnly = true)
    public Optional<OtpDto> getActive(String userId, OtpType type) {
        long now = Instant.now().toEpochMilli();
        return otpRepository
                .findFirstByUserIdAndTypeAndUsedAtIsNullAndExpiresAtGreaterThanOrderByCreatedAtDesc(
                        userId, type, now
                )
                .map(OtpDto::fromEntity);
    }

    /**
     * OTP doğrulama ve tüketme.
     * Başarılı ise:
     *  - OTP.usedAt set edilir
     *  - OTP.destination null’lanır (istersen kaydı silebilirsin)
     *  - Kullanıcının ilgili alanı güncellenir (email/phone) + verified flag true yapılır.
     */
    public OtpVerifyResponse verifyAndConsume(String userId, OtpVerifyRequest req) {
        if (req == null || req.getType() == null || req.getCode() == null) {
            return OtpVerifyResponse.of(false);
        }

        long now = Instant.now().toEpochMilli();
        Optional<Otp> opt = otpRepository
                .findFirstByUserIdAndTypeAndUsedAtIsNullAndExpiresAtGreaterThanOrderByCreatedAtDesc(
                        userId, req.getType(), now
                );

        if (opt.isEmpty()) return OtpVerifyResponse.of(false);

        Otp active = opt.get();
        if (!active.getCode().equals(req.getCode())) {
            return OtpVerifyResponse.of(false);
        }

        // User’ı getir
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found: " + userId));

        // Destination’a göre kullanıcıyı güncelle
        if (req.getType() == OtpType.EMAIL_VERIFY) {
            // e-posta değişikliği akışı: destination’daki e-postayı kalıcı yap
            if (active.getDestination() != null && !active.getDestination().isBlank()) {
                user.setEmail(active.getDestination());
            }
            // doğrulama bayrağı (alan adları senden farklıysa uyarlayabilirsin)
            try {
                user.setEmailVerified(true);
            } catch (Exception ignored) {}
            userRepository.save(user);
        } else if (req.getType() == OtpType.PHONE_VERIFY) {
            if (active.getDestination() != null && !active.getDestination().isBlank()) {
                user.setPhone(active.getDestination());
            }
            try {
                user.setPhoneVerified(true);
            } catch (Exception ignored) {}
            userRepository.save(user);
        }

        // OTP’yi tüket
        active.setUsedAt(now);
        active.setDestination(""); // doğrulandı, artık gerekli değil
        otpRepository.save(active);

        // (opsiyonel) aynı user+type için kalan diğer kayıtları temizlemek istersen:
        // otpRepository.deleteByUserIdAndType(userId, req.getType());

        return OtpVerifyResponse.of(true);
    }

    /** 6 haneli yalnızca rakam içeren kod üretir (100000-999999 arası). */
    private String generate6DigitNumericCode() {
        int num = 100_000 + new Random().nextInt(900_000);
        return Integer.toString(num);
    }
}
