package com.hospital.smart_hospital.auth.dto.request;



import com.hospital.smart_hospital.common.enums.BloodGroup;
import com.hospital.smart_hospital.common.enums.Gender;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RegisterPatientRequest(

        @NotBlank(message = "Full name is required.")
        String fullName,

        @Email(message = "Invalid email.")
        @NotBlank(message = "Email is required.")
        String email,

        @NotBlank(message = "Password is required.")
        @Size(min = 8, max = 20)
        String password,

        @NotBlank(message = "Phone number is required.")
        String phoneNumber,

        @NotNull(message = "Date of birth is required.")
        LocalDate dateOfBirth,

        @NotNull(message = "Gender is required.")
        Gender gender,

        @NotNull(message = "Blood group is required.")
        BloodGroup bloodGroup,

        String address,

        @NotBlank(message = "Emergency contact name is required.")
        String emergencyContactName,

        @NotBlank(message = "Emergency contact number is required.")
        String emergencyContactNumber

) {
}