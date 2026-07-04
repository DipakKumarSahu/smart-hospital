package com.hospital.smart_hospital.department.entity;


import com.hospital.smart_hospital.common.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;



    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name = "departments")
    public class Department extends AuditableEntity {

        @Column(name = "department_code",
                nullable = false,
                unique = true,
                length = 20)
        private String departmentCode;

        @Column(name = "department_name",
                nullable = false,
                unique = true,
                length = 100)
        private String departmentName;

        @Column(length = 500)
        private String description;

        @Builder.Default
        private boolean active = true;
    }

