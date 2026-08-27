package com.hospital.smart_hospital.auth.repository;

import com.hospital.smart_hospital.auth.entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationTokenRepository
        extends JpaRepository<EmailVerificationToken, Long> {

    Optional<EmailVerificationToken>
    findByTokenAndUsedFalse(String token);

    Optional<EmailVerificationToken>
    findByUserId(Long userId);

    void deleteByUserId(Long userId);
}