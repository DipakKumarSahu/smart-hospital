package com.hospital.smart_hospital.auth.service.impl;




import com.hospital.smart_hospital.auth.dto.request.RegisterPatientRequest;
import com.hospital.smart_hospital.exception.BadRequestException;
import com.hospital.smart_hospital.exception.ResourceNotFoundException;
import com.hospital.smart_hospital.patient.entity.Patient;
import com.hospital.smart_hospital.patient.repository.PatientRepository;
import com.hospital.smart_hospital.role.Role;
import com.hospital.smart_hospital.role.RoleType;
import com.hospital.smart_hospital.role.repositories.RoleRepository;
import com.hospital.smart_hospital.auth.dto.request.LoginRequest;

import com.hospital.smart_hospital.auth.dto.response.LoginResponse;
import com.hospital.smart_hospital.security.jwt.JwtService;
import com.hospital.smart_hospital.security.principal.CustomUserPrincipal;
import com.hospital.smart_hospital.auth.service.AuthenticationService;
import com.hospital.smart_hospital.user.User;
import com.hospital.smart_hospital.user.enums.AuthProvider;
import com.hospital.smart_hospital.user.enums.UserStatus;
import com.hospital.smart_hospital.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationServiceImpl emailVerificationService;

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.email(),
                                request.password()
                        )
                );

        CustomUserPrincipal principal =
                (CustomUserPrincipal) authentication.getPrincipal();

        String accessToken = jwtService.generateToken(principal);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .build();
    }

    @Override
    public void registerPatient(RegisterPatientRequest request) {

        validateEmail(request.email());

        Role patientRole = getPatientRole();

        User user = buildUser(request, patientRole);

        user = userRepository.save(user);

        Patient patient = buildPatient(request, user);

        patientRepository.save(patient);

        emailVerificationService.createAndSendVerificationToken(user.getId());
    }

    // =====================================================
    // Helper Methods
    // =====================================================

    private void validateEmail(String email) {

        if (userRepository.existsByEmail(email)) {

            throw new BadRequestException(
                    "Email already registered."
            );
        }
    }

    private Role getPatientRole() {

        return roleRepository.findByName(RoleType.ROLE_PATIENT)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient role not found."
                        ));
    }

    private User buildUser(RegisterPatientRequest request,
                           Role patientRole) {

        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .phoneNumber(request.phoneNumber())
                .provider(AuthProvider.LOCAL)
                .status(UserStatus.ACTIVE)
                .emailVerified(false)
                .build();

        user.getRoles().add(patientRole);

        return user;
    }

    private Patient buildPatient(RegisterPatientRequest request,
                                 User user) {

        Patient patient = new Patient();

        patient.setPatientCode(generatePatientCode());
        patient.setUser(user);
        patient.setDateOfBirth(request.dateOfBirth());
        patient.setGender(request.gender());
        patient.setBloodGroup(request.bloodGroup());
        patient.setAddress(request.address());
        patient.setEmergencyContactName(
                request.emergencyContactName());
        patient.setEmergencyContactNumber(
                request.emergencyContactNumber());
        patient.setActive(true);

        return patient;
    }

    private String generatePatientCode() {

        long count = patientRepository.count() + 1;

        return String.format("PAT-%06d", count);
    }
}