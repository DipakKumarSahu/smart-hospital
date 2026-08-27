package com.hospital.smart_hospital.appointment.entity;


import com.hospital.smart_hospital.appointment_slot.entity.AppointmentSlot;
import com.hospital.smart_hospital.common.entity.AuditableEntity;
import com.hospital.smart_hospital.common.enums.AppointmentStatus;
import com.hospital.smart_hospital.common.enums.BookingSource;
import com.hospital.smart_hospital.common.enums.PaymentStatus;
import com.hospital.smart_hospital.common.enums.VisitType;
import com.hospital.smart_hospital.doctor.entity.Doctor;
import com.hospital.smart_hospital.patient.entity.Patient;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(
        name = "appointments",
        indexes = {
                @Index(name = "idx_patient", columnList = "patient_id"),
                @Index(name = "idx_doctor", columnList = "doctor_id"),
                @Index(name = "idx_slot", columnList = "appointment_slot_id"),
                @Index(
                        name = "idx_appointment_status",
                        columnList = "appointment_status"
                ),
                @Index(name = "idx_date", columnList = "appointment_date")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment extends AuditableEntity {

    @Column(nullable = false, unique = true, length = 20)
    private String appointmentCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "appointment_slot_id",
            nullable = false,
            unique = true
    )
    private AppointmentSlot appointmentSlot;

    @Column(nullable = false)
    private LocalDate appointmentDate;

    @Column(nullable = false)
    private LocalTime appointmentTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VisitType visitType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingSource bookingSource;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private AppointmentStatus status = AppointmentStatus.BOOKED;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus appointmentStatus = AppointmentStatus.PAYMENT_PENDING;

    @Column(nullable = false, length = 500)
    private String reasonForVisit;

    @Column(length = 1000)
    private String notes;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "payment_expires_at")
    private LocalDateTime paymentExpiresAt;
}
