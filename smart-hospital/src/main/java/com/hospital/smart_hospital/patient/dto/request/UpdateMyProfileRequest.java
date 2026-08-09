package com.hospital.smart_hospital.patient.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateMyProfileRequest(

        @NotBlank(message = "Phone number is required.")
        @Size(max = 15)
        String phoneNumber,

        @Size(max = 500)
        String address,

        @NotBlank(message = "Emergency contact name is required.")
        @Size(max = 100)
        String emergencyContactName,

        @NotBlank(message = "Emergency contact number is required.")
        @Size(max = 15)
        String emergencyContactNumber,

        String profileImageUrl

) {
}
