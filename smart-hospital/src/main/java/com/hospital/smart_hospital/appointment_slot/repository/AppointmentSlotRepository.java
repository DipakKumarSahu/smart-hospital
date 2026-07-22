package com.hospital.smart_hospital.appointment_slot.repository;




import com.hospital.smart_hospital.appointment_slot.entity.AppointmentSlot;
import com.hospital.smart_hospital.common.enums.SlotStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentSlotRepository
        extends JpaRepository<AppointmentSlot, Long> {

    Optional<AppointmentSlot> findByIdAndActiveTrue(Long id);

    Page<AppointmentSlot> findByActiveTrue(Pageable pageable);

    Page<AppointmentSlot> findByDoctorIdAndSlotDateAndActiveTrue(
            Long doctorId,
            LocalDate slotDate,
            Pageable pageable
    );

    List<AppointmentSlot> findByDoctorIdAndSlotDateAndStatusAndActiveTrue(
            Long doctorId,
            LocalDate slotDate,
            SlotStatus status
    );

    boolean existsByDoctorIdAndSlotDateAndStartTimeAndEndTimeAndActiveTrue(
            Long doctorId,
            LocalDate slotDate,
            LocalTime startTime,
            LocalTime endTime
    );

    List<AppointmentSlot> findByDoctorScheduleIdAndActiveTrue(
            Long doctorScheduleId
    );

    void deleteByDoctorScheduleId(Long doctorScheduleId);
}