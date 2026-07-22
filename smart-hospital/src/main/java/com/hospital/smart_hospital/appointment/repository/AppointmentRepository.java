package com.hospital.smart_hospital.appointment.repository;



import com.hospital.smart_hospital.appointment.entity.Appointment;
import com.hospital.smart_hospital.common.enums.AppointmentStatus;
import com.hospital.smart_hospital.common.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Optional<Appointment> findByIdAndActiveTrue(Long id);

    Optional<Appointment> findByAppointmentCodeAndActiveTrue(String appointmentCode);

    Page<Appointment> findByActiveTrue(Pageable pageable);

    Page<Appointment> findByPatientUserFullNameContainingIgnoreCaseAndActiveTrue(
            String keyword,
            Pageable pageable
    );

    Page<Appointment> findByDoctorStaffUserFullNameContainingIgnoreCaseAndActiveTrue(
            String keyword,
            Pageable pageable
    );

    List<Appointment> findByDoctorIdAndAppointmentDateAndActiveTrue(
            Long doctorId,
            LocalDate appointmentDate
    );

    List<Appointment> findByPatientIdAndActiveTrue(Long patientId);

    List<Appointment> findByAppointmentDateAndStatusAndActiveTrue(
            LocalDate appointmentDate,
            AppointmentStatus status
    );

    List<Appointment> findByPaymentStatusAndActiveTrue(
            PaymentStatus paymentStatus
    );

    boolean existsByAppointmentSlotId(Long appointmentSlotId);
}