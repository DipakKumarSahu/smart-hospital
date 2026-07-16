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
public class UpdateDoctorRequest {

    @NotBlank(message = "Specialization is required.")
    private String specialization;

    @NotNull(message = "Experience is required.")
    @Min(value = 0)
    private Integer experienceYears;

    @NotNull(message = "Consultation fee is required.")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal consultationFee;

    @NotNull(message = "Department is required.")
    private Long departmentId;

    private boolean active;
}