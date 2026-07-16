package com.hospital.smart_hospital.department.dto.request;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDepartmentRequest {

    @NotBlank(message = "Department name is required.")
    @Size(max = 100)
    private String departmentName;

    @Size(max = 500)
    private String description;

    private boolean active;
}