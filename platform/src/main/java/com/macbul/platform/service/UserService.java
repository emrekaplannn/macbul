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
        String norm = normalizeEmail(email);
        User u = userRepository.findByEmail(norm)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(u.getEmail())
                .password(u.getPasswordHash())
                .roles("USER") // role tablosu yok; default USER
                .accountLocked(false).disabled(false).build();
    }

    // === Domain ===
    public UserDto createUser(UserCreateRequest request) {
        String email = normalizeEmail(request.getEmail());
        String phone = normalizePhone(request.getPhone());

        userRepository.findByEmail(email).ifPresent(u -> {
            throw new IllegalArgumentException("Bu e-posta zaten kayıtlı.");
        });
        if (phone != null && !phone.isBlank()) {
            userRepository.findByPhone(phone).ifPresent(u -> {
                throw new IllegalArgumentException("Bu telefon numarası zaten kayıtlı.");
            });
        }

        User newUser = new User();
        newUser.setId(UUID.randomUUID().toString());
        newUser.setEmail(email);
        newUser.setPhone(phone);
        newUser.setPasswordHash(passwordEncoder.encode(request.getPassword())); // BCrypt
        newUser.setRegisteredAt(System.currentTimeMillis());
        newUser.setOverallScore(null);
        newUser.setIsBanned(false);
        newUser.setReferredByCode(request.getReferredByCode());
        // doğrulama bayrakları
        newUser.setEmailVerified(false);
        newUser.setPhoneVerified(false);

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

        // e-posta güncelleme (çakışma kontrolü + verify reset)
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            String newEmail = normalizeEmail(request.getEmail());
            if (!newEmail.equalsIgnoreCase(existing.getEmail())) {
                userRepository.findByEmail(newEmail).ifPresent(u -> {
                    if (!u.getId().equals(id)) throw new IllegalArgumentException("Bu e-posta zaten kayıtlı.");
                });
                existing.setEmail(newEmail);
                existing.setEmailVerified(false); // e-posta değiştiğinde doğrulama sıfırlanır
            }
        }

        // telefon güncelleme (çakışma kontrolü + verify reset)
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            String newPhone = normalizePhone(request.getPhone());
            if (newPhone == null || newPhone.isBlank()) {
                throw new IllegalArgumentException("Telefon formatı geçersiz.");
            }
            if (existing.getPhone() == null || !newPhone.equals(existing.getPhone())) {
                userRepository.findByPhone(newPhone).ifPresent(u -> {
                    if (!u.getId().equals(id)) throw new IllegalArgumentException("Bu telefon numarası zaten kayıtlı.");
                });
                existing.setPhone(newPhone);
                existing.setPhoneVerified(false); // telefon değiştiğinde doğrulama sıfırlanır
            }
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

    // === EMAIL VERIFY ===
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
            .orElseThrow(() -> new ResourceNotFoundException("Kod bulunamadı veya kullanıcı eşleşmedi"));

        if (otp.getUsedAt() != null) throw new BadRequestException("Code already used");
        long now = System.currentTimeMillis();
        if (otp.getExpiresAt() != null && otp.getExpiresAt() < now) throw new BadRequestException("Code expired");

        UserDto userDto = verifyEmail(userId);
        otp.setUsedAt(now);
        otpRepository.save(otp);
        return userDto;
    }

    // === PHONE VERIFY ===
    public UserDto verifyPhone(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        if (user.getPhone() == null || user.getPhone().isBlank()) {
            throw new IllegalArgumentException("Kullanıcının kayıtlı bir telefonu yok.");
        }
        if (Boolean.FALSE.equals(user.getPhoneVerified())) {
            user.setPhoneVerified(true);
            userRepository.save(user);
        }
        return mapperUtil.toUserDto(user);
    }

    /** İstersen ayrı bir PhoneVerificationRequest DTO'su tanımlayabilirsin;
     *  burada aynı yapı (sadece code) olduğu için EmailVerificationRequest kullanıldı. */
    public UserDto verifyPhoneByCode(EmailVerificationRequest request, String userId) throws BadRequestException {
        if (userId == null || userId.isBlank()) throw new BadRequestException("User ID is required");

        Otp otp = otpRepository
            .findByUserIdAndCodeAndType(userId, request.getCode(), OtpType.PHONE_VERIFY)
            .orElseThrow(() -> new ResourceNotFoundException("Kod bulunamadı veya kullanıcı eşleşmedi"));

        if (otp.getUsedAt() != null) throw new BadRequestException("Code already used");
        long now = System.currentTimeMillis();
        if (otp.getExpiresAt() != null && otp.getExpiresAt() < now) throw new BadRequestException("Code expired");

        UserDto userDto = verifyPhone(userId);
        otp.setUsedAt(now);
        otpRepository.save(otp);
        return userDto;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(normalizeEmail(email));
    }

    // === Helpers ===
    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    private String normalizePhone(String phone) {
        if (phone == null) return null;
        // basit normalize: boşlukları sil
        String p = phone.trim().replace(" ", "");
        return p.isEmpty() ? null : p;
    }
}
