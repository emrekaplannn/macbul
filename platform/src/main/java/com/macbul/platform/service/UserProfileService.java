package com.macbul.platform.service;

import com.macbul.platform.dto.UserProfileCreateRequest;
import com.macbul.platform.dto.UserProfileDto;
import com.macbul.platform.dto.UserProfileUpdateRequest;
import com.macbul.platform.exception.ResourceNotFoundException;
import com.macbul.platform.model.District;
import com.macbul.platform.model.User;
import com.macbul.platform.model.UserProfile;
import com.macbul.platform.repository.DistrictRepository;
import com.macbul.platform.repository.UserProfileRepository;
import com.macbul.platform.repository.UserRepository;
import com.macbul.platform.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private MapperUtil mapperUtil;


    /* ===================== CREATE PROFILE ===================== */

    public UserProfileDto createProfile(UserProfileCreateRequest request, String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        if (userProfileRepository.existsById(userId)) {
            throw new IllegalArgumentException("Profile already exists for user: " + userId);
        }

        UserProfile profile = new UserProfile();
        profile.setUser(user);

        profile.setFullName(request.getFullName());
        profile.setPosition(request.getPosition());
        profile.setAvatarPath(request.getAvatarPath());
        profile.setBio(request.getBio());

        // DISTRICT ASSIGNMENT
        if (request.getDistrictId() != null) {
            District district = districtRepository.findById(request.getDistrictId())
                    .orElseThrow(() -> new ResourceNotFoundException("District not found: " + request.getDistrictId()));
            profile.setDistrict(district);
        }

        UserProfile saved = userProfileRepository.save(profile);
        return mapperUtil.toUserProfileDto(saved);
    }


    /* ===================== GET PROFILE ===================== */

    public UserProfileDto getProfileByUserId(String userId) {
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user: " + userId));

        return mapperUtil.toUserProfileDto(profile);
    }


    /* ===================== LIST PROFILES ===================== */

    public List<UserProfileDto> getAllProfiles() {
        return userProfileRepository.findAll()
                .stream()
                .map(mapperUtil::toUserProfileDto)
                .collect(Collectors.toList());
    }


    /* ===================== UPDATE PROFILE ===================== */

    public UserProfileDto updateProfile(String userId, UserProfileUpdateRequest request) {

        UserProfile existing = userProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user: " + userId));

        if (request.getFullName() != null)
            existing.setFullName(request.getFullName());

        if (request.getPosition() != null)
            existing.setPosition(request.getPosition());

        if (request.getAvatarPath() != null)
            existing.setAvatarPath(request.getAvatarPath());

        if (request.getBio() != null)
            existing.setBio(request.getBio());

        // DISTRICT UPDATE
        if (request.getDistrictId() != null) {
            District district = districtRepository.findById(request.getDistrictId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("District not found: " + request.getDistrictId()));
            existing.setDistrict(district);
        } else if (request.getDistrictId() == null) {
            // Kullanıcı district kaldırmak istiyorsa (null gönderildiyse)
            existing.setDistrict(null);
        }

        UserProfile updated = userProfileRepository.save(existing);
        return mapperUtil.toUserProfileDto(updated);
    }


    /* ===================== DELETE PROFILE ===================== */

    public void deleteProfile(String userId) {
        if (!userProfileRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Profile not found for user: " + userId);
        }
        userProfileRepository.deleteById(userId);
    }


    /* ===================== AVATAR HELPERS ===================== */

    public String getAvatarPath(String userId) {
        return userProfileRepository.findById(userId)
                .map(UserProfile::getAvatarPath)
                .orElse(null);
    }

    public UserProfile updateAvatarPath(String userId, String path) {
        UserProfile p = userProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user: " + userId));
        p.setAvatarPath(path);
        return userProfileRepository.save(p);
    }
}
