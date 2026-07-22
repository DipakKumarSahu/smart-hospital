//package com.hospital.smart_hospital.appointment.entity;
//
//import com.hospital.smart_hospital.common.entity.AuditableEntity;
//import com.hospital.smart_hospital.common.enums.AppointmentStatus;
//import com.hospital.smart_hospital.common.enums.BookingSource;
//import com.hospital.smart_hospital.common.enums.PaymentStatus;
//import com.hospital.smart_hospital.common.enums.VisitType;
//import com.hospital.smart_hospital.doctor.entity.Doctor;
//import com.hospital.smart_hospital.patient.entity.Patient;
//import com.hospital.smart_hospital.schedule.enitiy.AppointmentSlot;
//import jakarta.persistence.*;
//import lombok.*;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Entity
//@Table(
//        name = "appointments",
//        uniqueConstraints = {
//                @UniqueConstraint(
//                        name = "uk_appointment_code",
//                        columnNames = "appointment_code"
//                ),
//                @UniqueConstraint(
//                        name = "uk_appointment_slot",
//                        columnNames = "appointment_slot_id"
//                )
//        }
//)
//public class Appointment extends AuditableEntity {
//
//    @Column(name = "appointment_code",
//            nullable = false,
//            length = 30)
//    private String appointmentCode;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(
//            name = "patient_id",
//            nullable = false,
//            foreignKey = @ForeignKey(name = "fk_appointment_patient")
//    )
//    private Patient patient;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(
//            name = "doctor_id",
//            nullable = false,
//            foreignKey = @ForeignKey(name = "fk_appointment_doctor")
//    )
//    private Doctor doctor;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(
//            name = "appointment_slot_id",
//            nullable = false,
//            unique = true,
//            foreignKey = @ForeignKey(name = "fk_appointment_slot")
//    )
//    private AppointmentSlot appointmentSlot;
//
//    @Enumerated(EnumType.STRING)
//    @Builder.Default
//    @Column(nullable = false)
//    private AppointmentStatus status =
//            AppointmentStatus.PAYMENT_PENDING;
//
//    @Enumerated(EnumType.STRING)
//    @Builder.Default
//    @Column(name = "payment_status", nullable = false)
//    private PaymentStatus paymentStatus =
//            PaymentStatus.PENDING;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "visit_type", nullable = false)
//    private VisitType visitType;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "booking_source", nullable = false)
//    private BookingSource bookingSource;
//
//    @Column(length = 1000)
//    private String symptoms;
//
//    @Column(length = 1000)
//    private String notes;
//
//    @Builder.Default
//    @Column(nullable = false)
//    private boolean active = true;
//}
//
