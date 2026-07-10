package com.hospital.smart_hospital.schedule.enitiy;




import com.hospital.smart_hospital.appointment.entity.Appointment;
import com.hospital.smart_hospital.common.entity.AuditableEntity;
import com.hospital.smart_hospital.common.enums.SlotStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "appointment_slots",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_schedule_slot",
                        columnNames = {
                                "doctor_schedule_id",
                                "slot_date",
                                "start_time"
                        }
                )
        })
public class AppointmentSlot extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "doctor_schedule_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_slot_schedule")
    )
    private DoctorSchedule doctorSchedule;

    @Column(name = "slot_date", nullable = false)
    private LocalDate slotDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private SlotStatus status = SlotStatus.AVAILABLE;

    @OneToOne(mappedBy = "appointmentSlot",
            fetch = FetchType.LAZY)
    private Appointment appointment;
}
