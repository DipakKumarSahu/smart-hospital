package com.hospital.smart_hospital.staff.dto.request;

import com.hospital.smart_hospital.role.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateStaffRequest(

        @NotBlank(message = "Full name is required.")
        String fullName,

        @Email(message = "Invalid email format.")
        @NotBlank(message = "Email is required.")
        String email,

        @NotBlank(message = "Phone number is required.")
        String phoneNumber,

        @NotBlank(message = "Password is required.")
        @Size(min = 6, message = "Password must be at least 6 characters.")
        String password,

        @NotNull(message = "Role is required.")
        RoleType role, // ROLE_DOCTOR, ROLE_NURSE, etc.

        String designation
) {}