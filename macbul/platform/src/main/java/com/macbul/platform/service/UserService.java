package com.macbul.platform.service;

import com.macbul.platform.dto.UserCreateRequest;
import com.macbul.platform.dto.UserDto;
import com.macbul.platform.exception.ResourceNotFoundException;
import com.macbul.platform.model.User;
import com.macbul.platform.repository.UserRepository;
import com.macbul.platform.util.MapperUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MapperUtil mapperUtil;


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
}
