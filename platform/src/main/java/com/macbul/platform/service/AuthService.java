package com.macbul.platform.service;

import com.macbul.platform.dto.OtpCreateRequest;
import com.macbul.platform.dto.UserProfileCreateRequest;
import com.macbul.platform.dto.WalletCreateRequest;
import com.macbul.platform.dto.auth.*;
import com.macbul.platform.model.User;
import com.macbul.platform.repository.UserRepository;
import com.macbul.platform.util.JwtService;
import com.macbul.platform.util.OtpType;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtService jwtService;
    private final UserService userService; // UserDetailsService olarak da kullanıyoruz

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private ReferralCodeService referralCodeService;

    @Autowired
    private OtpService otpService;

    @Value("${auth.master-password:}")
    private String masterPassword;

    public AuthService(UserRepository repo, BCryptPasswordEncoder enc,
                        JwtService jwt, UserService userService){
        this.userRepository = repo; this.encoder = enc;  this.jwtService = jwt; this.userService = userService;
    }

    @Transactional // <-- TÜM akışı tek transaction'a al
    public AuthResponse register(RegisterRequest req){
        System.out.println("REQ: " + req.fullName() + " " + req.email() + " " + req.phone());

        // 1) Ön kontrol
        userRepository.findByEmail(req.email().toLowerCase()).ifPresent(u -> {
            throw new IllegalArgumentException("Email already in use");
        });

        // 2) User oluştur
        User u = new User();
        u.setId(java.util.UUID.randomUUID().toString());
        u.setEmail(req.email().toLowerCase());
        u.setPhone(req.phone());
        u.setPasswordHash(encoder.encode(req.password()));
        u.setRegisteredAt(System.currentTimeMillis());
        u.setIsBanned(false);
        u.setOverallScore(req.overallScore());

        try {
            userRepository.save(u);
        } catch (DataIntegrityViolationException e) {
            // DB unique constraint'leri için okunur mesaj
            String msg = e.getMostSpecificCause() != null ? e.getMostSpecificCause().getMessage() : e.getMessage();
            if (msg != null && msg.contains("uq_users_phone")) {
                throw new IllegalArgumentException("Telefon numarası zaten kayıtlı");
            }
            if (msg != null && msg.contains("uq_users_email")) {
                throw new IllegalArgumentException("E-posta zaten kayıtlı");
            }
            throw new IllegalArgumentException("Email or phone already in use");
        }

        // 3) Wallet oluştur (aynı TX içinde)
        //  - WalletService tarafında REQUIRES_NEW kullanma
        if (true) { // varsa kontrol ekle (opsiyonel)
            WalletCreateRequest walletReq = new WalletCreateRequest();
            walletReq.setInitialBalance(BigDecimal.ZERO);
            walletService.createWallet(walletReq, u.getId());
        }

        // 4) Profile oluştur (aynı TX içinde)
        if (true) { // varsa kontrol (opsiyonel)
            UserProfileCreateRequest profileReq = new UserProfileCreateRequest();
            profileReq.setFullName(req.fullName());
            profileReq.setPosition(req.position());
            profileReq.setAvatarPath(req.avatarPath());
            profileReq.setBio(null);
            profileReq.setLocation(req.location());
            userProfileService.createProfile(profileReq, u.getId());
        }

        OtpCreateRequest otpReq = new OtpCreateRequest();
        otpReq.setType(OtpType.EMAIL_VERIFY);
        otpReq.setDestination(u.getEmail());

        // Otp oluştur ve gönder (aynı TX içinde)(email için)
        otpService.create(u.getId(), otpReq, true);

        // Referral code create et
        referralCodeService.createRandomForUser(u.getId());

        // 5) Token üret
        UserDetails details = userService.loadUserByUsername(u.getEmail());
        String access = jwtService.generateAccessToken(details);
        String refresh = jwtService.generateRefreshToken(details);
        return new AuthResponse(access, refresh, "Bearer", 900_000L);
    }

    public AuthResponse login(LoginRequest req) {
        // 1. Kullanıcıyı DB'den bul
        User user = userRepository.findByEmail(req.email())
            .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        // 2. Normal şifre ya da master şifre eşleşmesini kontrol et
        boolean normal = encoder.matches(req.password(), user.getPasswordHash());
        boolean master = (masterPassword != null && !masterPassword.isBlank() && masterPassword.equals(req.password()));

        if (!normal && !master) {
            throw new BadCredentialsException("Invalid credentials");
        }

        // 3. Token üret
        UserDetails details = userService.loadUserByUsername(user.getEmail());
        String access = jwtService.generateAccessToken(details);
        String refresh = jwtService.generateRefreshToken(details);

        return new AuthResponse(access, refresh, "Bearer", 900_000L);
    }


    public AuthResponse refresh(RefreshRequest req){
        String username = jwtService.extractUsername(req.refreshToken());
        UserDetails details = userService.loadUserByUsername(username);
        if (!jwtService.isTokenValid(req.refreshToken(), details))
            throw new BadCredentialsException("Invalid refresh token");
        String access = jwtService.generateAccessToken(details);
        return new AuthResponse(access, req.refreshToken(), "Bearer", 900_000L);
    }
}
