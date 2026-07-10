package com.hospital.smart_hospital.schedule.repository;




import com.hospital.smart_hospital.common.enums.SlotStatus;
import com.hospital.smart_hospital.schedule.enitiy.AppointmentSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentSlotRepository
        extends JpaRepository<AppointmentSlot, Long> {

    List<AppointmentSlot> findByDoctorScheduleDoctorIdAndSlotDate(
            Long doctorId,
            LocalDate slotDate
    );

    List<AppointmentSlot> findByDoctorScheduleDoctorIdAndSlotDateAndStatus(
            Long doctorId,
            LocalDate slotDate,
            SlotStatus status
    );

    Optional<AppointmentSlot> findByIdAndStatus(
            Long id,
            SlotStatus status
    );

    boolean existsByDoctorScheduleIdAndSlotDate(
            Long doctorScheduleId,
            LocalDate slotDate
    );
}
