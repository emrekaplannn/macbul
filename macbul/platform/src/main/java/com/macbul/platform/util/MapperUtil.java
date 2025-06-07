// src/main/java/com/macbul/platform/util/MapperUtil.java
package com.macbul.platform.util;

import com.macbul.platform.dto.MatchDto;
import com.macbul.platform.dto.TeamDto;
import com.macbul.platform.dto.TransactionDto;
import com.macbul.platform.dto.UserDto;
import com.macbul.platform.dto.UserProfileCreateRequest;
import com.macbul.platform.dto.UserProfileDto;
import com.macbul.platform.dto.WalletCreateRequest;
import com.macbul.platform.dto.WalletDto;
import com.macbul.platform.model.Match;
import com.macbul.platform.model.Team;
import com.macbul.platform.model.Transaction;
import com.macbul.platform.model.User;
import com.macbul.platform.model.UserProfile;
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
}
