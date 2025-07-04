// src/main/java/com/macbul/platform/util/MapperUtil.java
package com.macbul.platform.util;

import com.macbul.platform.dto.FeedbackDto;
import com.macbul.platform.dto.MatchDto;
import com.macbul.platform.dto.MatchParticipantDto;
import com.macbul.platform.dto.MatchVideoDto;
import com.macbul.platform.dto.NotificationDto;
import com.macbul.platform.dto.ReferralCodeDto;
import com.macbul.platform.dto.ReferralDto;
import com.macbul.platform.dto.ReportDto;
import com.macbul.platform.dto.TeamDto;
import com.macbul.platform.dto.TransactionDto;
import com.macbul.platform.dto.UserDto;
import com.macbul.platform.dto.UserProfileCreateRequest;
import com.macbul.platform.dto.UserProfileDto;
import com.macbul.platform.dto.VideoClipDto;
import com.macbul.platform.dto.WalletCreateRequest;
import com.macbul.platform.dto.WalletDto;
import com.macbul.platform.model.Feedback;
import com.macbul.platform.model.Match;
import com.macbul.platform.model.MatchParticipant;
import com.macbul.platform.model.MatchVideo;
import com.macbul.platform.model.Notification;
import com.macbul.platform.model.Referral;
import com.macbul.platform.model.ReferralCode;
import com.macbul.platform.model.Report;
import com.macbul.platform.model.Team;
import com.macbul.platform.model.Transaction;
import com.macbul.platform.model.User;
import com.macbul.platform.model.UserProfile;
import com.macbul.platform.model.VideoClip;
import com.macbul.platform.model.Wallet;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MapperUtil {

    private final ModelMapper modelMapper;

    public MapperUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserDto toUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    // New: map UserProfile → UserProfileDto
    public UserProfileDto toUserProfileDto(UserProfile profile) {
        return modelMapper.map(profile, UserProfileDto.class);
    }

    // Optional: if you ever need to convert a CreateRequest into an entity
    public UserProfile toUserProfileEntity(UserProfileCreateRequest request) {
        return modelMapper.map(request, UserProfile.class);
    }

     // --- New mapping for Wallet → WalletDto ---
    public WalletDto toWalletDto(Wallet wallet) {
        WalletDto dto = new WalletDto();
        dto.setId(wallet.getId());
        dto.setUserId(wallet.getUser().getId());
        dto.setBalance(wallet.getBalance());
        dto.setUpdatedAt(wallet.getUpdatedAt());
        return dto;
    }

    // Optionally map CreateRequest → Wallet entity (partial)
    public Wallet toWalletEntity(WalletCreateRequest request, User user) {
        Wallet wallet = new Wallet();
        wallet.setId(request.getUserId()); // We’ll use userId as the UUID or generate separately in Service
        wallet.setUser(user);
        wallet.setBalance(request.getInitialBalance());
        wallet.setUpdatedAt(System.currentTimeMillis());
        return wallet;
    }

    /** Transaction → TransactionDto */
    public TransactionDto toTransactionDto(Transaction tx) {
        TransactionDto dto = new TransactionDto();
        dto.setId(tx.getId());
        dto.setUserId(tx.getUser().getId());
        dto.setAmount(tx.getAmount());
        dto.setType(tx.getType());
        dto.setDescription(tx.getDescription());
        dto.setCreatedAt(tx.getCreatedAt());
        return dto;
    }

    public TeamDto toTeamDto(Team team) {
        TeamDto dto = new TeamDto();
        dto.setId(team.getId());
        dto.setMatchId(team.getMatch().getId());
        dto.setTeamNumber(team.getTeamNumber());
        dto.setAverageScore(team.getAverageScore());
        dto.setCreatedAt(team.getCreatedAt());
        return dto;
    }

    public MatchDto toMatchDto(Match match) {
        MatchDto dto = new MatchDto();
        dto.setId(match.getId());
        dto.setOrganizerId(match.getOrganizer().getId());
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
        MatchParticipantDto dto = new MatchParticipantDto();
        dto.setId(mp.getId());
        dto.setMatchId(mp.getMatch().getId());
        dto.setUserId(mp.getUser().getId());
        dto.setTeamId(mp.getTeam() != null ? mp.getTeam().getId() : null);
        dto.setJoinedAt(mp.getJoinedAt());
        dto.setHasPaid(mp.getHasPaid());
        return dto;
    }


    public MatchVideoDto toMatchVideoDto(MatchVideo mv) {
        MatchVideoDto dto = new MatchVideoDto();
        dto.setId(mv.getId());
        dto.setMatchId(mv.getMatch().getId());
        dto.setVideoUrl(mv.getVideoUrl());
        dto.setUploadedAt(mv.getUploadedAt());
        return dto;
    }


    public ReferralDto toReferralDto(Referral r) {
        ReferralDto dto = new ReferralDto();
        dto.setId(r.getId());
        dto.setReferrerUserId(r.getReferrerUser().getId());
        dto.setReferredUserId(r.getReferredUser().getId());
        dto.setMatchId(r.getMatch() != null ? r.getMatch().getId() : null);
        dto.setRewardAmount(r.getRewardAmount());
        dto.setRewarded(r.getRewarded());
        dto.setCreatedAt(r.getCreatedAt());
        return dto;
    }



    public ReferralCodeDto toReferralCodeDto(ReferralCode rc) {
        ReferralCodeDto dto = new ReferralCodeDto();
        dto.setId(rc.getId());
        dto.setUserId(rc.getUser().getId());
        dto.setCode(rc.getCode());
        dto.setCreatedAt(rc.getCreatedAt());
        return dto;
    }


    public FeedbackDto toFeedbackDto(Feedback fb) {
        FeedbackDto dto = new FeedbackDto();
        dto.setId(fb.getId());
        dto.setMatchId(fb.getMatch().getId());
        dto.setUserId(fb.getUser().getId());
        dto.setRating(fb.getRating());
        dto.setComment(fb.getComment());
        dto.setCreatedAt(fb.getCreatedAt());
        return dto;
    }    

    public NotificationDto toNotificationDto(Notification n) {
        NotificationDto dto = new NotificationDto();
        dto.setId(n.getId());
        dto.setUserId(n.getUser().getId());
        dto.setType(n.getType());
        dto.setPayload(n.getPayload());
        dto.setIsRead(n.getIsRead());
        dto.setCreatedAt(n.getCreatedAt());
        return dto;
    }


    public ReportDto toReportDto(Report r) {
        ReportDto dto = new ReportDto();
        dto.setId(r.getId());
        dto.setMatchId(r.getMatch().getId());
        dto.setReporterUserId(r.getReporter().getId());
        dto.setReportedUserId(r.getReported().getId());
        dto.setReason(r.getReason());
        dto.setDetails(r.getDetails());
        dto.setStatus(r.getStatus());
        dto.setCreatedAt(r.getCreatedAt());
        dto.setResolvedAt(r.getResolvedAt());
        return dto;
    }


    public VideoClipDto toVideoClipDto(VideoClip vc) {
        VideoClipDto dto = new VideoClipDto();
        dto.setId(vc.getId());
        dto.setMatchVideoId(vc.getMatchVideo().getId());
        dto.setUserId(vc.getUser().getId());
        dto.setClipUrl(vc.getClipUrl());
        dto.setStartSec(vc.getStartSec());
        dto.setEndSec(vc.getEndSec());
        dto.setCreatedAt(vc.getCreatedAt());
        return dto;
    }
}
