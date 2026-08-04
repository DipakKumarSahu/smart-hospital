package com.hospital.smart_hospital.security.service.impl;



import com.hospital.smart_hospital.doctor.entity.Doctor;
import com.hospital.smart_hospital.doctor.repository.DoctorRepository;
import com.hospital.smart_hospital.exception.ResourceNotFoundException;
import com.hospital.smart_hospital.patient.entity.Patient;
import com.hospital.smart_hospital.patient.repository.PatientRepository;
import com.hospital.smart_hospital.security.principal.CustomUserPrincipal;
import com.hospital.smart_hospital.security.service.CurrentUserService;
import com.hospital.smart_hospital.staff.entity.Staff;
import com.hospital.smart_hospital.staff.repository.StaffRepository;
import com.hospital.smart_hospital.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CurrentUserServiceImpl implements CurrentUserService {

    private final StaffRepository staffRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public User getCurrentUser() {

        return getPrincipal().getUser();
    }

    @Override
    public Long getCurrentUserId() {

        return getPrincipal().getUserId();
    }

    @Override
    public String getCurrentUserEmail() {

        return getPrincipal().getUsername();
    }

    @Override
    public Staff getCurrentStaff() {

        return staffRepository
                .findByUserIdAndActiveTrue(getCurrentUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Staff not found."
                        ));
    }

    @Override
    public Patient getCurrentPatient() {

        return patientRepository
                .findByUserIdAndActiveTrue(getCurrentUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found."
                        ));
    }

    @Override
    public Doctor getCurrentDoctor() {

        Staff staff = getCurrentStaff();

        return doctorRepository
                .findByStaffIdAndActiveTrue(staff.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found."
                        ));
    }

    private CustomUserPrincipal getPrincipal() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {

            throw new ResourceNotFoundException(
                    "No authenticated user found."
            );
        }

        return (CustomUserPrincipal) authentication.getPrincipal();
    }
}