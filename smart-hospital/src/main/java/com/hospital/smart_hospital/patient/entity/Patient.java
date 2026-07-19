package com.hospital.smart_hospital.patient.entity;


import com.hospital.smart_hospital.common.entity.AuditableEntity;
import com.hospital.smart_hospital.common.enums.BloodGroup;
import com.hospital.smart_hospital.common.enums.Gender;
import com.hospital.smart_hospital.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "patients",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_patient_code",
                        columnNames = "patient_code"
                ),
                @UniqueConstraint(
                        name = "uk_patient_user",
                        columnNames = "user_id"
                )
        }
)
public class Patient extends AuditableEntity {

    @Column(name = "patient_code",
            nullable = false,
            length = 30)
    private String patientCode;

    @Column(name = "date_of_birth",
            nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_group",
            nullable = false)
    private BloodGroup bloodGroup;

    @Column(length = 500)
    private String address;

    @Column(name = "emergency_contact_name",
            nullable = false,
            length = 100)
    private String emergencyContactName;

    @Column(name = "emergency_contact_number",
            nullable = false,
            length = 15)
    private String emergencyContactNumber;

//    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true,
            foreignKey = @ForeignKey(name = "fk_patient_user")
    )
    private User user;
}
