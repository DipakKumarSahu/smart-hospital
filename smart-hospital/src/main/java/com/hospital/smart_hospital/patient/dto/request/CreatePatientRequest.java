package com.hospital.smart_hospital.patient.dto.request;



import com.hospital.smart_hospital.common.enums.BloodGroup;
import com.hospital.smart_hospital.common.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

    @Getter
    @Setter
    public class CreatePatientRequest {

        @NotNull(message = "User id is required.")
        private Long userId;

        @NotNull(message = "Date of birth is required.")
        @Past(message = "Date of birth must be in the past.")
        private LocalDate dateOfBirth;

        @NotNull(message = "Gender is required.")
        private Gender gender;

        @NotNull(message = "Blood group is required.")
        private BloodGroup bloodGroup;

        private String address;

        @NotBlank(message = "Emergency contact name is required.")
        private String emergencyContactName;

        @NotBlank(message = "Emergency contact number is required.")
        private String emergencyContactNumber;
    }

