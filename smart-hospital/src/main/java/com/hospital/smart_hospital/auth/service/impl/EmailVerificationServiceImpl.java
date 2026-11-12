package com.hospital.smart_hospital.auth.service.impl;



import com.hospital.smart_hospital.auth.entity.EmailVerificationToken;
import com.hospital.smart_hospital.auth.repository.EmailVerificationTokenRepository;

import com.hospital.smart_hospital.auth.service.EmailVerificationService;
import com.hospital.smart_hospital.exception.BadRequestException;
import com.hospital.smart_hospital.exception.ResourceNotFoundException;
import com.hospital.smart_hospital.integration.email.service.EmailService;
import com.hospital.smart_hospital.user.User;
import com.hospital.smart_hospital.user.enums.UserStatus;
import com.hospital.smart_hospital.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailVerificationServiceImpl
        implements EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

//    @Value("${app.frontend.url}")
//    private String frontendUrl;

    @Value("${app.backend.url}")
    private String backendUrl;

    private static final int TOKEN_EXPIRY_MINUTES = 30;

    @Override
    public void createAndSendVerificationToken(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found."
                        ));

        /*
         * Remove an existing token first.
         */
        tokenRepository.deleteByUserId(userId);

        String token = UUID.randomUUID().toString();

        EmailVerificationToken verificationToken =
                EmailVerificationToken.builder()
                        .token(token)
                        .user(user)
                        .expiresAt(
                                LocalDateTime.now()
                                        .plusMinutes(
                                                TOKEN_EXPIRY_MINUTES
                                        )
                        )
                        .used(false)
                        .build();

        tokenRepository.save(verificationToken);

//        String verificationLink =                         // For Frontend
//                frontendUrl
//                        + "/verify-email?token="
//                        + token;

        String verificationLink =
                backendUrl
                        + "/api/v1/auth/verify-email?token="
                        + token;

        String html = buildVerificationEmail(
                user.getFullName(),
                verificationLink
        );

        emailService.sendEmail(
                user.getEmail(),
                "Verify your Smart Hospital account",
                html
        );
    }

    @Override
//    public void verifyEmail(String token) {
//
//        EmailVerificationToken verificationToken =
//                tokenRepository
//                        .findByTokenAndUsedFalse(token)
//                        .orElseThrow(() ->
//                                new BadRequestException(
//                                        "Invalid or expired verification token."
//                                ));
//
//        if (verificationToken.getExpiresAt()
//                .isBefore(LocalDateTime.now())) {
//
//            throw new BadRequestException(
//                    "Verification token has expired."
//            );
//        }
//
//        User user = verificationToken.getUser();
//
//        user.setEmailVerified(true);
//
//        /*
//         * Account becomes active after email verification.
//         *
//         * Phone verification will later be added here.
//         */
//        user.setStatus(UserStatus.ACTIVE);
//
//        verificationToken.setUsed(true);
//
//        userRepository.save(user);
//        tokenRepository.save(verificationToken);
//    }

    // New for frontend
    public void verifyEmail(String token) {
        EmailVerificationToken verificationToken = tokenRepository
                .findByTokenAndUsedFalse(token)
                .orElseThrow(() -> new BadRequestException("Invalid or expired verification token."));

        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Verification token has expired.");
        }

        User user = verificationToken.getUser();
        user.setEmailVerified(true);
        user.setStatus(UserStatus.ACTIVE);
        verificationToken.setUsed(true);

        userRepository.save(user);
        tokenRepository.save(verificationToken);
    }

    private String buildVerificationEmail(
            String fullName,
            String verificationLink) {

        return """
                <!DOCTYPE html>
                <html>
                <body>
                    <h2>Welcome to Smart Hospital</h2>

                    <p>Hello %s,</p>

                    <p>
                        Thank you for registering with Smart Hospital.
                        Please verify your email address by clicking
                        the button below.
                    </p>

                    <p>
                        <a href="%s"
                           style="
                               display:inline-block;
                               padding:12px 20px;
                               background:#1976d2;
                               color:white;
                               text-decoration:none;
                               border-radius:5px;
                           ">
                            Verify Email
                        </a>
                    </p>

                    <p>
                        This verification link will expire in
                        %d minutes.
                    </p>

                    <p>
                        If you did not create this account,
                        you can safely ignore this email.
                    </p>

                    <p>
                        Regards,<br>
                        Smart Hospital
                    </p>
                </body>
                </html>
                """.formatted(
                fullName,
                verificationLink,
                TOKEN_EXPIRY_MINUTES
        );
    }

    @Override
    // 👇 NAYA METHOD: Doctor ke liye Welcome Email
    public void sendDoctorWelcomeEmail(String fullName, String email, String rawPassword) {
        String html = """
                <!DOCTYPE html>
                <html>
                <body style="font-family: sans-serif; color: #333;">
                    <h2>Welcome to Smart Hospital</h2>
                    <p>Hello Dr. %s,</p>
                    <p>Your Doctor account has been created successfully by the Admin.</p>
                    <p>Here are your login credentials:</p>
                    <table style="border-collapse: collapse; margin: 15px 0;">
                        <tr>
                            <td style="padding: 8px; border: 1px solid #ddd; background: #f9f9f9;"><strong>Email:</strong></td>
                            <td style="padding: 8px; border: 1px solid #ddd;">%s</td>
                        </tr>
                        <tr>
                            <td style="padding: 8px; border: 1px solid #ddd; background: #f9f9f9;"><strong>Password:</strong></td>
                            <td style="padding: 8px; border: 1px solid #ddd;">%s</td>
                        </tr>
                    </table>
                    <p>Please login to the portal and change your password for security.</p>
                    <p>Regards,<br>Smart Hospital Admin</p>
                </body>
                </html>
                """.formatted(fullName, email, rawPassword);

        try {
            // 👇 Dhyan se check karo: (to, subject, body) order mein hona chahiye
            emailService.sendEmail(email, "Welcome to Smart Hospital - Doctor Account", html);
        } catch (Exception e) {
            // Agar email fail ho jaye, toh log karo par aage badho
            System.err.println("Failed to send welcome email to: " + email);
            e.printStackTrace();
        }
    }
}