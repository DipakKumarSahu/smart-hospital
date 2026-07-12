package com.hospital.smart_hospital.department.dto.response;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponse {

    private Long id;

    private String departmentCode;

    private String departmentName;

    private String description;

    private boolean active;

}