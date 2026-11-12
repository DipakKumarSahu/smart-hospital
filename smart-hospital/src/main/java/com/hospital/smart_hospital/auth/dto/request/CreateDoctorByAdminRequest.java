package com.hospital.smart_hospital.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;

public record CreateDoctorByAdminRequest(
        // User fields
        @NotBlank(message = "Full name is required.")
        String fullName,
        @Email(message = "Invalid email.")
        @NotBlank(message = "Email is required.")
        String email,
        @NotBlank(message = "Phone number is required.")
        String phoneNumber,
        @NotBlank(message = "Password is required.")
        String password,

        // Staff fields
        @NotBlank(message = "Designation is required.")
        String designation,

        // Doctor fields
        @NotNull(message = "Department is required.")
        Long departmentId,
        @NotBlank(message = "Specialization is required.")
        String specialization,
        @NotBlank(message = "License number is required.")
        String licenseNumber,
        @NotNull(message = "Experience is required.")
        @Min(value = 0, message = "Experience cannot be negative.")
        Integer experienceYears,
        @NotNull(message = "Consultation fee is required.")
        BigDecimal consultationFee
) {}