package com.hospital.smart_hospital.prescription.entity;



import com.hospital.smart_hospital.appointment.entity.Appointment;
import com.hospital.smart_hospital.common.entity.AuditableEntity;
import com.hospital.smart_hospital.doctor.entity.Doctor;
import com.hospital.smart_hospital.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "prescriptions",
        indexes = {
                @Index(name = "idx_prescription_code", columnList = "prescription_code"),
                @Index(name = "idx_prescription_patient", columnList = "patient_id"),
                @Index(name = "idx_prescription_doctor", columnList = "doctor_id"),
                @Index(name = "idx_prescription_appointment", columnList = "appointment_id")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Prescription extends AuditableEntity {

    @Column(name = "prescription_code",
            nullable = false,
            unique = true,
            length = 30)
    private String prescriptionCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "patient_id",
            nullable = false
    )
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "doctor_id",
            nullable = false
    )
    private Doctor doctor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "appointment_id",
            nullable = false,
            unique = true
    )
    private Appointment appointment;

    @Column(nullable = false, length = 1000)
    private String diagnosis;

    @Column(length = 1000)
    private String advice;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(
            mappedBy = "prescription",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<PrescriptionItem> items = new ArrayList<>();
}