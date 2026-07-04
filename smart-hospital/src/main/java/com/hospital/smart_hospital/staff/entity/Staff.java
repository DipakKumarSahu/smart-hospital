package com.hospital.smart_hospital.staff.entity;


import com.hospital.smart_hospital.common.entity.AuditableEntity;
import com.hospital.smart_hospital.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "staff",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_staff_employee_code", columnNames = "employee_code")
        }
)
public class Staff extends AuditableEntity {

    @Column(name = "employee_code", nullable = false, length = 30)
    private String employeeCode;

//    @Column(name = "full_name", nullable = false, length = 100)
//    private String fullName;
//
//    @Column(name = "phone_number", nullable = false, length = 15)
//    private String phoneNumber;

    @Column(name = "joining_date", nullable = false)
    private LocalDate joiningDate;

    @Column(name = "designation", nullable = false, length = 100)
    private String designation;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true,
            foreignKey = @ForeignKey(name = "fk_staff_user")
    )
    private User user;
}