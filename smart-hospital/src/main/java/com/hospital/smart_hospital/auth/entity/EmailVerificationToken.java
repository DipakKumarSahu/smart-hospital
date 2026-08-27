package com.hospital.smart_hospital.auth.entity;

import com.hospital.smart_hospital.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "email_verification_tokens",
        indexes = {
                @Index(
                        name = "idx_email_verification_token",
                        columnList = "token"
                ),
                @Index(
                        name = "idx_email_verification_user",
                        columnList = "user_id"
                )
        }
)
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            unique = true,
            length = 100
    )
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean used = false;
}