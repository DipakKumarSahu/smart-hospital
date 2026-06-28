package com.hospital.smart_hospital.user;

import com.hospital.smart_hospital.common.entity.BaseEntity;
import com.hospital.smart_hospital.role.Role;
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
public class UserEntity extends BaseEntity {




        @Column(unique = true, nullable = false)
        private String email;

        private String password;

        private String provider;

        @Enumerated(EnumType.STRING)
        @Column(length = 30)
        private UserStatus status;

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "user_roles",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id")
        )
        private Set<Role> roles = new HashSet<>();
    }

