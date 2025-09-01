// src/main/java/com/macbul/platform/repository/ReferralCodeRepository.java
package com.macbul.platform.repository;

import com.macbul.platform.model.ReferralCode;
import com.macbul.platform.util.ReferralCodeStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

/**
 * CRUD + lookup-by-user and lookup-by-code for ReferralCode.
 */
public interface ReferralCodeRepository extends JpaRepository<ReferralCode, String> {

    List<ReferralCode> findByUserId(String userId);

    Optional<ReferralCode> findByCode(String code);

    Optional<ReferralCode> findFirstByUserIdAndStatusOrderByCreatedAtDesc(String userId, ReferralCodeStatus status);

    List<ReferralCode> findByUserIdAndStatus(String userId, ReferralCodeStatus status);

}
