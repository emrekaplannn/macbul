// src/main/java/com/macbul/platform/util/MapperUtil.java
package com.macbul.platform.util;

import com.macbul.platform.dto.*;
import com.macbul.platform.model.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MapperUtil {

    private final ModelMapper modelMapper;

    public MapperUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /* ---------- User ---------- */

    public UserDto toUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    /* ---------- UserProfile (explicit to avoid ambiguity) ---------- */

    public UserProfileDto toUserProfileDto(UserProfile profile) {
        if (profile == null) return null;
        UserProfileDto dto = new UserProfileDto();
        // IMPORTANT: take id ONLY from UserProfile.userId (not from user.id)
        dto.setUserId(profile.getUserId());
        dto.setFullName(profile.getFullName());
        dto.setPosition(profile.getPosition());
        dto.setAvatarUrl(profile.getAvatarUrl());
        dto.setBio(profile.getBio());
        return dto;
    }

    public UserProfile toUserProfileEntity(UserProfileCreateRequest request) {
        if (request == null) return null;
        // If you need more control, map manually; for now ModelMapper is ok here
        return modelMapper.map(request, UserProfile.class);
    }

    /* ---------- Wallet ---------- */

    public WalletDto toWalletDto(Wallet wallet) {
        if (wallet == null) return null;
        WalletDto dto = new WalletDto();
        dto.setId(wallet.getId());
        dto.setUserId(wallet.getUser() != null ? wallet.getUser().getId() : null);
        dto.setBalance(wallet.getBalance());
        dto.setUpdatedAt(wallet.getUpdatedAt());
        return dto;
    }

    public Wallet toWalletEntity(WalletCreateRequest request, User user) {
        if (request == null) return null;
        Wallet wallet = new Wallet();
        wallet.setId(request.getUserId());        // or generate in Service
        wallet.setUser(user);
        wallet.setBalance(request.getInitialBalance());
        wallet.setUpdatedAt(System.currentTimeMillis());
        return wallet;
    }

    /* ---------- Transactions ---------- */

    public TransactionDto toTransactionDto(Transaction tx) {
        if (tx == null) return null;
        TransactionDto dto = new TransactionDto();
        dto.setId(tx.getId());
        dto.setUserId(tx.getUser() != null ? tx.getUser().getId() : null);
        dto.setAmount(tx.getAmount());
        dto.setType(tx.getType());
        dto.setDescription(tx.getDescription());
        dto.setCreatedAt(tx.getCreatedAt());
        return dto;
    }

    /* ---------- Teams ---------- */

    public TeamDto toTeamDto(Team team) {
        if (team == null) return null;
        TeamDto dto = new TeamDto();
        dto.setId(team.getId());
        dto.setMatchId(team.getMatch() != null ? team.getMatch().getId() : null);
        dto.setTeamNumber(team.getTeamNumber());
        dto.setAverageScore(team.getAverageScore());
        dto.setCreatedAt(team.getCreatedAt());
        return dto;
    }

    /* ---------- Matches ---------- */

    public MatchDto toMatchDto(Match match) {
        if (match == null) return null;
        MatchDto dto = new MatchDto();
        dto.setId(match.getId());
        dto.setOrganizerId(match.getOrganizer() != null ? match.getOrganizer().getId() : null);
        dto.setFieldName(match.getFieldName());
        dto.setAddress(match.getAddress());
        dto.setCity(match.getCity());
        dto.setMatchTimestamp(match.getMatchTimestamp());
        dto.setPricePerUser(match.getPricePerUser());
        dto.setTotalSlots(match.getTotalSlots());
        dto.setCreatedAt(match.getCreatedAt());
        return dto;
    }

    public MatchParticipantDto toMatchParticipantDto(MatchParticipant mp) {
        if (mp == null) return null;
        MatchParticipantDto dto = new MatchParticipantDto();
        dto.setId(mp.getId());
        dto.setMatchId(mp.getMatch() != null ? mp.getMatch().getId() : null);
        dto.setUserId(mp.getUser() != null ? mp.getUser().getId() : null);
        dto.setTeamId(mp.getTeam() != null ? mp.getTeam().getId() : null);
        dto.setJoinedAt(mp.getJoinedAt());
        dto.setHasPaid(mp.getHasPaid());
        return dto;
    }

    public MatchVideoDto toMatchVideoDto(MatchVideo mv) {
        if (mv == null) return null;
        MatchVideoDto dto = new MatchVideoDto();
        dto.setId(mv.getId());
        dto.setMatchId(mv.getMatch() != null ? mv.getMatch().getId() : null);
        dto.setVideoUrl(mv.getVideoUrl());
        dto.setUploadedAt(mv.getUploadedAt());
        return dto;
    }

    /* ---------- Referrals ---------- */

    public ReferralDto toReferralDto(Referral r) {
        if (r == null) return null;
        ReferralDto dto = new ReferralDto();
        dto.setId(r.getId());
        dto.setReferrerUserId(r.getReferrerUser() != null ? r.getReferrerUser().getId() : null);
        dto.setReferredUserId(r.getReferredUser() != null ? r.getReferredUser().getId() : null);
        dto.setMatchId(r.getMatch() != null ? r.getMatch().getId() : null);
        dto.setRewardAmount(r.getRewardAmount());
        dto.setRewarded(r.getRewarded());
        dto.setCreatedAt(r.getCreatedAt());
        return dto;
    }

    public ReferralCodeDto toReferralCodeDto(ReferralCode rc) {
        if (rc == null) return null;
        ReferralCodeDto dto = new ReferralCodeDto();
        dto.setId(rc.getId());
        dto.setUserId(rc.getUser() != null ? rc.getUser().getId() : null);
        dto.setCode(rc.getCode());
        dto.setStatus(rc.getStatus()); 
        dto.setCreatedAt(rc.getCreatedAt());
        return dto;
    }

    /* ---------- Feedback / Notifications / Reports / Clips ---------- */

    public FeedbackDto toFeedbackDto(Feedback fb) {
        if (fb == null) return null;
        FeedbackDto dto = new FeedbackDto();
        dto.setId(fb.getId());
        dto.setMatchId(fb.getMatch() != null ? fb.getMatch().getId() : null);
        dto.setUserId(fb.getUser() != null ? fb.getUser().getId() : null);
        dto.setRating(fb.getRating());
        dto.setComment(fb.getComment());
        dto.setCreatedAt(fb.getCreatedAt());
        return dto;
    }

    public NotificationDto toNotificationDto(Notification n) {
        if (n == null) return null;
        NotificationDto dto = new NotificationDto();
        dto.setId(n.getId());
        dto.setUserId(n.getUser() != null ? n.getUser().getId() : null);
        dto.setType(n.getType());
        dto.setPayload(n.getPayload());
        dto.setIsRead(n.getIsRead());
        dto.setCreatedAt(n.getCreatedAt());
        return dto;
    }

    public ReportDto toReportDto(Report r) {
        if (r == null) return null;
        ReportDto dto = new ReportDto();
        dto.setId(r.getId());
        dto.setMatchId(r.getMatch() != null ? r.getMatch().getId() : null);
        dto.setReporterUserId(r.getReporter() != null ? r.getReporter().getId() : null);
        dto.setReportedUserId(r.getReported() != null ? r.getReported().getId() : null);
        dto.setReason(r.getReason());
        dto.setDetails(r.getDetails());
        dto.setStatus(r.getStatus());
        dto.setCreatedAt(r.getCreatedAt());
        dto.setResolvedAt(r.getResolvedAt());
        return dto;
    }

    public VideoClipDto toVideoClipDto(VideoClip vc) {
        if (vc == null) return null;
        VideoClipDto dto = new VideoClipDto();
        dto.setId(vc.getId());
        dto.setMatchVideoId(vc.getMatchVideo() != null ? vc.getMatchVideo().getId() : null);
        dto.setUserId(vc.getUser() != null ? vc.getUser().getId() : null);
        dto.setClipUrl(vc.getClipUrl());
        dto.setStartSec(vc.getStartSec());
        dto.setEndSec(vc.getEndSec());
        dto.setCreatedAt(vc.getCreatedAt());
        return dto;
    }
}
