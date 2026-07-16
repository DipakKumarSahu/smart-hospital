package com.hospital.smart_hospital.doctor.dto.response;


import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class DoctorResponse {

    private Long id;

    private String doctorCode;

    private String licenseNumber;

    private String specialization;

    private Integer experienceYears;

    private BigDecimal consultationFee;

    private boolean active;

    private Long departmentId;

    private String departmentName;

    private Long staffId;

    private String staffName;
}