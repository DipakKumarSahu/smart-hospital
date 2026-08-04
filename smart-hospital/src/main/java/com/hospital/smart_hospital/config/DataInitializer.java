package com.hospital.smart_hospital.config;


import com.hospital.smart_hospital.role.Role;
import com.hospital.smart_hospital.role.RoleType;
import com.hospital.smart_hospital.role.repositories.RoleRepository;
import com.hospital.smart_hospital.user.User;
import com.hospital.smart_hospital.user.enums.AuthProvider;
import com.hospital.smart_hospital.user.enums.UserStatus;
import com.hospital.smart_hospital.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Set;


@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        log.info("Initializing application data...");

        createRoles();

        createDefaultAdmin();

        log.info("Application data initialized successfully.");
    }

    private void createRoles() {

        Arrays.stream(RoleType.values())
                .forEach(roleType -> {

                    roleRepository.findByName(roleType)
                            .orElseGet(() ->
                                    roleRepository.save(
                                            Role.builder()
                                                    .name(roleType)
                                                    .build()
                                    ));
                });
    }

    private void createDefaultAdmin() {

        if (userRepository.existsByEmail("admin@hospital.com")) {
            return;
        }

        Role adminRole = roleRepository.findByName(RoleType.ROLE_ADMIN)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "ROLE_ADMIN not found."
                        ));

        User admin = User.builder()
                .fullName("System Administrator")
                .email("admin@hospital.com")
                .password(passwordEncoder.encode("Admin@123"))
                .phoneNumber("9999999999")
                .provider(AuthProvider.LOCAL)
                .status(UserStatus.ACTIVE)
                .emailVerified(true)
                .roles(Set.of(adminRole))
                .build();

        userRepository.save(admin);

        System.out.println();
        System.out.println("==========================================");
        log.info("Default admin user created with email: {}", admin.getEmail());
        System.out.println("Email    : admin@hospital.com");
        System.out.println("Password : Admin@123");
        System.out.println("==========================================");
        System.out.println();

    }
}