package com.hospital.smart_hospital.doctor.entity;



import com.hospital.smart_hospital.common.entity.AuditableEntity;
import com.hospital.smart_hospital.department.entity.Department;
import com.hospital.smart_hospital.schedule.enitiy.DoctorSchedule;
import com.hospital.smart_hospital.staff.entity.Staff;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "doctors",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_doctor_code", columnNames = "doctor_code"),
                @UniqueConstraint(name = "uk_license_number", columnNames = "license_number"),
                @UniqueConstraint(name = "uk_staff_id", columnNames = "staff_id")
        }
)
public class Doctor extends AuditableEntity {

    @Column(name = "doctor_code", nullable = false, length = 30)
    private String doctorCode;

    @Column(name = "license_number", nullable = false, length = 100)
    private String licenseNumber;

    @Column(nullable = false, length = 100)
    private String specialization;

    @Column(name = "experience_years", nullable = false)
    private Integer experienceYears;

    @Column(name = "consultation_fee",
            nullable = false,
            precision = 10,
            scale = 2)
    private BigDecimal consultationFee;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "staff_id",
            nullable = false,
            unique = true,
            foreignKey = @ForeignKey(name = "fk_doctor_staff")
    )
    private Staff staff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "department_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_doctor_department")
    )
    private Department department;

    @OneToMany(mappedBy = "doctor",
            fetch = FetchType.LAZY)
    private List<DoctorSchedule> schedules = new ArrayList<>();
}