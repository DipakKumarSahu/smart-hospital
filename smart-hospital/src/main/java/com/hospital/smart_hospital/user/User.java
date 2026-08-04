package com.hospital.smart_hospital.user;

import com.hospital.smart_hospital.common.entity.AuditableEntity;
import com.hospital.smart_hospital.patient.entity.Patient;
import com.hospital.smart_hospital.role.Role;
import com.hospital.smart_hospital.staff.entity.Staff;
import com.hospital.smart_hospital.user.enums.AuthProvider;
import com.hospital.smart_hospital.user.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends AuditableEntity {

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;


    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
        private String email;


    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    private String profileImageUrl;

        @Builder.Default
        @Enumerated(EnumType.STRING)
        private AuthProvider provider =  AuthProvider.LOCAL;

        @Builder.Default
        @Enumerated(EnumType.STRING)
        @Column(length = 30)
        private UserStatus status =  UserStatus.PENDING_VERIFICATION;

    @Builder.Default
    @Column(nullable = false)
    private boolean emailVerified = false;

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "user_roles",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id")
        )
        @Builder.Default
        private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user",
            fetch = FetchType.LAZY)
    private Staff staff;

    @OneToOne(mappedBy = "user",
            fetch = FetchType.LAZY)
    private Patient patient;

    }

