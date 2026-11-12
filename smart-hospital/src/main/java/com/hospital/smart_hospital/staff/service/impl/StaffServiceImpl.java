package com.hospital.smart_hospital.staff.service.impl;

import com.hospital.smart_hospital.exception.BadRequestException;
import com.hospital.smart_hospital.exception.ResourceNotFoundException;
import com.hospital.smart_hospital.integration.email.service.EmailService;
import com.hospital.smart_hospital.role.Role;
import com.hospital.smart_hospital.role.repositories.RoleRepository;
import com.hospital.smart_hospital.staff.dto.request.CreateStaffRequest;
import com.hospital.smart_hospital.staff.dto.response.StaffResponse;
import com.hospital.smart_hospital.staff.entity.Staff;
import com.hospital.smart_hospital.staff.repository.StaffRepository;
import com.hospital.smart_hospital.staff.service.StaffService;
import com.hospital.smart_hospital.user.User;
import com.hospital.smart_hospital.user.enums.AuthProvider;
import com.hospital.smart_hospital.user.enums.UserStatus;
import com.hospital.smart_hospital.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public void createStaff(CreateStaffRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email already registered.");
        }

        Role role = roleRepository.findByName(request.role())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found."));

        // 1. Save User
        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .password(passwordEncoder.encode(request.password()))
                .provider(AuthProvider.LOCAL)
                .status(UserStatus.ACTIVE)
                .emailVerified(true)
                .build();
        user.getRoles().add(role);
        user = userRepository.save(user);

        // 2. Save Staff
        Staff staff = Staff.builder()
                .user(user)
                .employeeCode("EMP-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase())
                .designation(request.designation())
                .joiningDate(LocalDate.now())
                .active(true)
                .build();
        staffRepository.save(staff);

        // 3. Send Welcome Email
        String emailBody = "Hello " + request.fullName() + ",\n\n" +
                "Welcome to Our Hospital! Your staff account has been created by the Admin.\n\n" +
                "Login Credentials:\n" +
                "Email: " + request.email() + "\n" +
                "Password: " + request.password() + "\n\n" +
                "Please login to the portal to access your dashboard.";

        emailService.sendEmail(user.getEmail(), "Welcome to Smart Hospital", emailBody);
    }

    @Override
    public Page<StaffResponse> getStaff(String keyword, Pageable pageable) {
        return staffRepository.searchStaff(keyword, pageable).map(staff -> {
            User user = staff.getUser();
            String role = user.getRoles().isEmpty() ? "N/A" : user.getRoles().iterator().next().getName().name();

            return StaffResponse.builder()
                    .id(staff.getId())
                    .employeeCode(staff.getEmployeeCode())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .designation(staff.getDesignation())
                    .active(staff.isActive())
                    .joiningDate(staff.getJoiningDate())
                    .role(role)
                    .build();
        });
    }

    @Override
    public void toggleStaffStatus(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));

        staff.setActive(!staff.isActive());
        staffRepository.save(staff);
    }

    @Override
    public Map<String, Long> getStaffStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", staffRepository.count());
        stats.put("active", staffRepository.countByActiveTrue());
        stats.put("inactive", staffRepository.countByActiveFalse());
        return stats;
    }
}