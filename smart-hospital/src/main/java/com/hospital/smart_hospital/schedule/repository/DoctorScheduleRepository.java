package com.hospital.smart_hospital.schedule.repository;

import com.hospital.smart_hospital.appointment_slot.entity.AppointmentSlot;
import com.hospital.smart_hospital.schedule.enitiy.DoctorSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface DoctorScheduleRepository
        extends JpaRepository<DoctorSchedule, Long> {

    /**
     * Get active schedule by id
     */
    Optional<DoctorSchedule> findByIdAndActiveTrue(Long id);

    /**
     * Get all active schedules
     */
    Page<DoctorSchedule> findByActiveTrue(Pageable pageable);

    /**
     * Search schedules by doctor's name
     */
    Page<DoctorSchedule> findByDoctorStaffUserFullNameContainingIgnoreCaseAndActiveTrue(
            String keyword,
            Pageable pageable
    );

    /**
     * Get doctor's schedule for a specific day.
     * Used while generating appointment slots.
     */
    Optional<DoctorSchedule> findByDoctorIdAndDayOfWeekAndActiveTrue(
            Long doctorId,
            DayOfWeek dayOfWeek
    );

    /**
     * Get all schedules of a doctor.
     */
    List<DoctorSchedule> findByDoctorIdAndActiveTrue(Long doctorId);

    /**
     * Check overlapping schedule while creating.
     */
    @Query("""
            SELECT COUNT(ds) > 0
            FROM DoctorSchedule ds
            WHERE ds.doctor.id = :doctorId
              AND ds.dayOfWeek = :dayOfWeek
              AND ds.active = true
              AND ds.startTime < :endTime
              AND ds.endTime > :startTime
            """)
    boolean existsOverlappingSchedule(
            @Param("doctorId") Long doctorId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    /**
     * Check overlapping schedule while updating.
     */
    @Query("""
            SELECT COUNT(ds) > 0
            FROM DoctorSchedule ds
            WHERE ds.doctor.id = :doctorId
              AND ds.dayOfWeek = :dayOfWeek
              AND ds.active = true
              AND ds.id <> :scheduleId
              AND ds.startTime < :endTime
              AND ds.endTime > :startTime
            """)
    boolean existsOverlappingSchedule(
            @Param("doctorId") Long doctorId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("scheduleId") Long scheduleId
    );


}