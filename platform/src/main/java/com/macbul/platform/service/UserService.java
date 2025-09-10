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
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired private UserRepository userRepository;
    @Autowired private MapperUtil mapperUtil;
    @Autowired private OtpRepository otpRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    // === Security: UserDetailsService ===
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User u = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(u.getEmail())
                .password(u.getPasswordHash())
                .roles("USER") // tablo yok; herkes USER
                .accountLocked(false).disabled(false).build();
    }

    // === Domain ===
    public UserDto createUser(UserCreateRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new IllegalArgumentException("Bu e-posta zaten kayıtlı.");
        });
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            userRepository.findByPhone(request.getPhone()).ifPresent(u -> {
                throw new IllegalArgumentException("Bu telefon numarası zaten kayıtlı.");
            });
        }

        User newUser = new User();
        newUser.setId(UUID.randomUUID().toString());
        newUser.setEmail(request.getEmail());
        newUser.setPhone(request.getPhone());
        // 🔒 BCrypt hash
        newUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        newUser.setRegisteredAt(System.currentTimeMillis());
        newUser.setOverallScore(null);
        newUser.setIsBanned(false);
        newUser.setReferredByCode(request.getReferredByCode());

        User saved = userRepository.save(newUser);
        return mapperUtil.toUserDto(saved);
    }

    public UserDto getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı: " + id));
        return mapperUtil.toUserDto(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(mapperUtil::toUserDto).toList();
    }

    public UserDto updateUser(String id, UserUpdateRequest request) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));

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

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }

    public UserDto verifyEmail(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        if (Boolean.FALSE.equals(user.getEmailVerified())) {
            user.setEmailVerified(true);
            userRepository.save(user);
        }
        return mapperUtil.toUserDto(user);
    }

    public UserDto verifyEmailByCode(EmailVerificationRequest request, String userId) throws BadRequestException {
        if (userId == null || userId.isBlank()) throw new BadRequestException("User ID is required");

        Otp otp = otpRepository
            .findByUserIdAndCodeAndType(userId, request.getCode(), OtpType.EMAIL_VERIFY)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        if (otp.getUsedAt() != null) throw new BadRequestException("Code already used");
        Long now = System.currentTimeMillis();
        if (otp.getExpiresAt() != null && otp.getExpiresAt() < now) throw new BadRequestException("Code expiredd");

        UserDto userDto = verifyEmail(userId);
        otp.setUsedAt(now);
        otpRepository.save(otp);
        return userDto;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase());
    }
}
