//package com.hospital.smart_hospital.appointment.repository;
//
//import com.hospital.smart_hospital.appointment.entity.Appointment;
//import com.hospital.smart_hospital.common.enums.AppointmentStatus;
//import com.hospital.smart_hospital.common.enums.PaymentStatus;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//public interface AppointmentRepository
//        extends JpaRepository<Appointment, Long> {
//
//    // ==========================
//    // Basic Queries
//    // ==========================
//
//    Optional<Appointment> findByIdAndActiveTrue(Long id);
//
//    Optional<Appointment> findByAppointmentCodeAndActiveTrue(
//            String appointmentCode
//    );
//
//    Page<Appointment> findByActiveTrue(Pageable pageable);
//
//    // ==========================
//    // Search
//    // ==========================
//
//    Page<Appointment>
//    findByPatientUserFullNameContainingIgnoreCaseAndActiveTrue(
//            String keyword,
//            Pageable pageable
//    );
//
//    Page<Appointment>
//    findByDoctorStaffUserFullNameContainingIgnoreCaseAndActiveTrue(
//            String keyword,
//            Pageable pageable
//    );
//
//    // ==========================
//    // Patient
//    // ==========================
//
//    List<Appointment> findByPatientIdAndActiveTrue(
//            Long patientId
//    );
//
//    Page<Appointment> findByPatientIdAndActiveTrue(
//            Long patientId,
//            Pageable pageable
//    );
//
//    // ==========================
//    // Doctor
//    // ==========================
//
//    List<Appointment>
//    findByDoctorIdAndAppointmentDateAndActiveTrue(
//            Long doctorId,
//            LocalDate appointmentDate
//    );
//
//    // ==========================
//    // Appointment Status
//    // ==========================
//
//    List<Appointment>
//    findByAppointmentDateAndStatusAndActiveTrue(
//            LocalDate appointmentDate,
//            AppointmentStatus status
//    );
//
//    // ==========================
//    // Payment
//    // ==========================
//
//    List<Appointment> findByAppointmentDateAndAppointmentStatusAndActiveTrue(
//        LocalDate appointmentDate,
//        AppointmentStatus appointmentStatus
//);
//
//    // ==========================
//    // Validation
//    // ==========================
//
//    boolean existsByAppointmentSlotIdAndActiveTrue(
//            Long appointmentSlotId
//    );
//}




package com.hospital.smart_hospital.appointment.repository;

import com.hospital.smart_hospital.appointment.entity.Appointment;
import com.hospital.smart_hospital.common.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {

    Optional<Appointment> findByIdAndActiveTrue(Long id);

    Optional<Appointment> findByAppointmentCodeAndActiveTrue(
            String appointmentCode
    );

    Page<Appointment> findByActiveTrue(Pageable pageable);

    Page<Appointment>
    findByPatientUserFullNameContainingIgnoreCaseAndActiveTrue(
            String keyword,
            Pageable pageable
    );

    Page<Appointment>
    findByDoctorStaffUserFullNameContainingIgnoreCaseAndActiveTrue(
            String keyword,
            Pageable pageable
    );

    List<Appointment> findByPatientIdAndActiveTrue(
            Long patientId
    );

    Page<Appointment> findByPatientIdAndActiveTrue(
            Long patientId,
            Pageable pageable
    );

    List<Appointment>
    findByDoctorIdAndAppointmentDateAndActiveTrue(
            Long doctorId,
            LocalDate appointmentDate
    );

    List<Appointment>
    findByAppointmentDateAndAppointmentStatusAndActiveTrue(
            LocalDate appointmentDate,
            AppointmentStatus appointmentStatus
    );

    boolean existsByAppointmentSlotIdAndActiveTrue(
            Long appointmentSlotId
    );
}