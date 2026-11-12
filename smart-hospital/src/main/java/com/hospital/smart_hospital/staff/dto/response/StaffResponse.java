package com.hospital.smart_hospital.staff.dto.response;

import com.hospital.smart_hospital.role.RoleType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class StaffResponse {
    private Long id;
    private String employeeCode;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String designation;
    private boolean active;
    private LocalDate joiningDate;
    private String role; // ROLE_DOCTOR, ROLE_NURSE, etc.
}