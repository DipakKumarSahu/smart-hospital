package com.hospital.smart_hospital.department.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDepartmentRequest {

    @NotBlank(message = "Department code is required.")
    @Size(
            max = 20,
            message = "Department code must not exceed 20 characters."
    )
    private String departmentCode;

    @NotBlank(message = "Department name is required.")
    @Size(
            max = 100,
            message = "Department name must not exceed 100 characters."
    )
    private String departmentName;

    @Size(
            max = 500,
            message = "Description must not exceed 500 characters."
    )
    private String description;
}