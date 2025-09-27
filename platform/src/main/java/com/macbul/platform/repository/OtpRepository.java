// com.macbul.platform.repository.OtpRepository.java
package com.macbul.platform.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.macbul.platform.model.Otp;
import com.macbul.platform.util.OtpType;

public interface OtpRepository extends JpaRepository<Otp, String> {
    Optional<Otp> findByUserIdAndCodeAndType(String userId, String code, OtpType type);

    // Latest (by createdAt) OTP for a user+type that is not used yet and not expired
    Optional<Otp> findFirstByUserIdAndTypeAndUsedAtIsNullAndExpiresAtGreaterThanOrderByCreatedAtDesc(
            String userId, OtpType type, Long nowEpochMs
    );

    // All OTPs for clean-up
    List<Otp> findByUserIdAndType(String userId, OtpType type);

    // Bulk delete helpers
    long deleteByUserIdAndType(String userId, OtpType type);
    long deleteByExpiresAtLessThan(Long nowEpochMs);

    Optional<Otp> findTopByUserIdAndTypeAndUsedAtIsNullAndExpiresAtAfterOrderByCreatedAtDesc(String userId,
            OtpType type, long now);

    List<Otp> findByUserIdAndTypeAndUsedAtIsNull(String userId, OtpType type);
}
