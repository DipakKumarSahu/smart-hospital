package com.hospital.smart_hospital.patient.dto.response;


import com.hospital.smart_hospital.common.enums.BloodGroup;
import com.hospital.smart_hospital.common.enums.Gender;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class PatientResponse {

    private Long id;

    private String patientCode;

    private String fullName;

    private String email;

    private String phoneNumber;

    private LocalDate dateOfBirth;

    private Gender gender;

    private BloodGroup bloodGroup;

    private String address;

    private String emergencyContactName;

    private String emergencyContactNumber;

    private boolean active;
}


