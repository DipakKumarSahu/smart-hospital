package com.hospital.smart_hospital.appointment_slot.entity;



import com.hospital.smart_hospital.common.entity.AuditableEntity;
import com.hospital.smart_hospital.common.enums.SlotStatus;
import com.hospital.smart_hospital.doctor.entity.Doctor;
import com.hospital.smart_hospital.schedule.enitiy.DoctorSchedule;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(
        name = "appointment_slots",
        indexes = {
                @Index(name = "idx_slot_date", columnList = "slot_date"),
                @Index(name = "idx_doctor", columnList = "doctor_id"),
                @Index(name = "idx_schedule", columnList = "doctor_schedule_id"),
                @Index(name = "idx_status", columnList = "status")
        }
)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSlot extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_schedule_id", nullable = false)
    private DoctorSchedule doctorSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private LocalDate slotDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlotStatus status = SlotStatus.AVAILABLE;

    @Column(nullable = false)
    private boolean active = true;
}
