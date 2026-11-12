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

//
import com.hospital.smart_hospital.auth.dto.request.CreateDoctorByAdminRequest;
import com.hospital.smart_hospital.staff.entity.Staff;
import com.hospital.smart_hospital.staff.repository.StaffRepository;
import com.hospital.smart_hospital.doctor.entity.Doctor;
import com.hospital.smart_hospital.doctor.repository.DoctorRepository;
import com.hospital.smart_hospital.department.repository.DepartmentRepository;
import com.hospital.smart_hospital.department.entity.Department;
import java.time.LocalDate;
import java.util.UUID;

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

    //
    private final StaffRepository staffRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

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

    // Methods for admin to create doc
    @Override
    public void createDoctorByAdmin(CreateDoctorByAdminRequest request) {

        // 1. Email aur License Validate karo
        validateEmail(request.email());
        if (doctorRepository.existsByLicenseNumberIgnoreCase(request.licenseNumber())) {
            throw new BadRequestException("License number already exists.");
        }

        // 2. Role aur Department nikaalo
        Role doctorRole = roleRepository.findByName(RoleType.ROLE_DOCTOR)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor role not found."));

        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found."));
        // 3. User banao
        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .phoneNumber(request.phoneNumber())
                .provider(AuthProvider.LOCAL)
                .status(UserStatus.ACTIVE)
                .emailVerified(true) // Admin ne banaya hai isliye verified true
                .build();
        user.getRoles().add(doctorRole);
        user = userRepository.save(user);
        // 4. Staff banao
        Staff staff = Staff.builder()
                .user(user)
                .employeeCode("EMP-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase())
                .designation(request.designation())
                .joiningDate(LocalDate.now())
                .active(true)
                .build();
        staff = staffRepository.save(staff);

//5. Doctor banao
        Doctor doctor = Doctor.builder()
                .staff(staff)
                .department(department)
                .specialization(request.specialization())
                .licenseNumber(request.licenseNumber())
                .experienceYears(request.experienceYears())
                .consultationFee(request.consultationFee())
                .doctorCode("DOC-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase())
                .active(true)
                .build();
        doctorRepository.save(doctor);
        // 6. Email bhejo (Welcome Email)
        String emailBody = "Hello " + request.fullName() + ",\n\n" +
                "Your Smart Hospital Doctor account has been created by Admin.\n" +
                "Email: " + request.email() + "\n" +
                "Password: " + request.password() + "\n\n" +
                "Please login to the portal.";

        emailVerificationService.sendDoctorWelcomeEmail(request.fullName(),request.email(),request.password());
    }
 }



