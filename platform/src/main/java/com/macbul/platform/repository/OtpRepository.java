// com.macbul.platform.repository.OtpRepository.java
package com.macbul.platform.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.macbul.platform.model.Otp;
import com.macbul.platform.util.OtpType;

public interface OtpRepository extends JpaRepository<Otp, String> {
    Optional<Otp> findByUserIdAndCodeAndType(String userId, String code, OtpType type);
}
