package com.hospital.smart_hospital.doctor.dto.request;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateDoctorRequest {

    @NotBlank(message = "License number is required.")
    private String licenseNumber;

    @NotBlank(message = "Specialization is required.")
    private String specialization;

    @NotNull(message = "Experience is required.")
    @Min(value = 0, message = "Experience cannot be negative.")
    private Integer experienceYears;

    @NotNull(message = "Consultation fee is required.")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Consultation fee must be greater than zero.")
    private BigDecimal consultationFee;

    @NotNull(message = "Department is required.")
    private Long departmentId;

    @NotNull(message = "Staff is required.")
    private Long staffId;
}
