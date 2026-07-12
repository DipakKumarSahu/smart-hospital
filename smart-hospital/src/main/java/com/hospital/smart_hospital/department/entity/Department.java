package com.hospital.smart_hospital.department.entity;


import com.hospital.smart_hospital.common.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;



@Getter
@Setter
//@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "departments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_department_code",
                        columnNames = "department_code"
                ),
                @UniqueConstraint(
                        name = "uk_department_name",
                        columnNames = "department_name"
                )
        }
)
public class Department extends AuditableEntity {

    @Column(name = "department_code",
            nullable = false,
            length = 20)
    private String departmentCode;

    @Column(name = "department_name",
            nullable = false,
            length = 100)
    private String departmentName;

    @Column(length = 500)
    private String description;

//    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;
}