// src/main/java/com/macbul/platform/repository/ReferralRepository.java
package com.macbul.platform.repository;

import com.macbul.platform.model.Referral;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * CRUD + listing by referrer/referred/match for Referral.
 */
public interface ReferralRepository extends JpaRepository<Referral, String> {

    List<Referral> findByReferrerUserId(String referrerUserId);
    List<Referral> findByReferredUserId(String referredUserId);
    List<Referral> findByMatchId(String matchId);
}
