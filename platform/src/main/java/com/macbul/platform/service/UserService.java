package com.macbul.platform.service;

import com.macbul.platform.dto.EmailVerificationRequest;
import com.macbul.platform.dto.UserCreateRequest;
import com.macbul.platform.dto.UserDto;
import com.macbul.platform.dto.UserUpdateRequest;
import com.macbul.platform.exception.ResourceNotFoundException;
import com.macbul.platform.model.Otp;
import com.macbul.platform.model.User;
import com.macbul.platform.repository.OtpRepository;
import com.macbul.platform.repository.UserRepository;
import com.macbul.platform.util.MapperUtil;
import com.macbul.platform.util.OtpType;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MapperUtil mapperUtil;

    @Autowired
    private OtpRepository otpRepository;

    public UserDto createUser(UserCreateRequest request) {
        // E-posta veya telefon zaten var mı kontrolü
        userRepository.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new IllegalArgumentException("Bu e-posta zaten kayıtlı.");
        });
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            userRepository.findByPhone(request.getPhone()).ifPresent(u -> {
                throw new IllegalArgumentException("Bu telefon numarası zaten kayıtlı.");
            });
        }

        // Entity nesnesini oluştur
        User newUser = new User();
        newUser.setId(UUID.randomUUID().toString());
        newUser.setEmail(request.getEmail());
        newUser.setPhone(request.getPhone());
        // NOT: Şifre hash’leme yok; yalın metin kaydediliyor. Üretimde mutlaka hash kullanın.
        newUser.setPasswordHash(request.getPassword());
        newUser.setRegisteredAt(System.currentTimeMillis());
        newUser.setOverallScore(null);
        newUser.setIsBanned(false);
        newUser.setReferredByCode(request.getReferredByCode());

        // Veritabanına kaydet
        User saved = userRepository.save(newUser);

        // DTO’ya dönüştür ve dön
        return mapperUtil.toUserDto(saved);
    }

   
    public UserDto getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı: " + id));
        return mapperUtil.toUserDto(user);
    }

    /**
     * Returns a list of all users.
     */
    public List<UserDto> getAllUsers() {
        // Örnek implementasyon:
        return userRepository.findAll()
                             .stream()
                             .map(mapperUtil::toUserDto)
                             .toList();
    }

    /**
     * Updates an existing user's email, phone, or referral code.
     */
    public UserDto updateUser(String id, UserUpdateRequest request) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));

        // Sadece non-null alanları güncelle
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            existing.setEmail(request.getEmail());
        }
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            existing.setPhone(request.getPhone());
        }
        if (request.getReferredByCode() != null) {
            existing.setReferredByCode(request.getReferredByCode());
        }

        User updated = userRepository.save(existing);
        return mapperUtil.toUserDto(updated);
    }


    /**
     * Deletes a user by ID.
     */
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }


    public UserDto verifyEmail(String userId) {
        // Fetch user entity by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        // Set email_verified to true
        if(!user.getEmailVerified()) {
            
            user.setEmailVerified(true);

            // Save updated entity
            userRepository.save(user);
        }

        // Return as DTO
        return mapperUtil.toUserDto(user);
    }

    // inside UserService.java
    public UserDto verifyEmailByCode(EmailVerificationRequest request) throws BadRequestException {
        String userId = request.getUserId();
        if (userId == null || userId.isBlank()) {
            throw new BadRequestException("User ID is required");
        }

        Otp otp = otpRepository
            .findByUserIdAndCodeAndType(userId, request.getCode(), OtpType.EMAIL_VERIFY)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        if (otp.getUsedAt() != null) {
            throw new BadRequestException("Code already used");
        }
        Long now = System.currentTimeMillis();
        if (otp.getExpiresAt() != null && otp.getExpiresAt() < now) {
            throw new BadRequestException("Code expiredd");
        }

        UserDto userDto =  verifyEmail(userId);

        otp.setUsedAt(now);
        otpRepository.save(otp);

        return userDto;
    }


}
