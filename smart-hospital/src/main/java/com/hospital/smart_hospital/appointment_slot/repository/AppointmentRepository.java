//package com.hospital.smart_hospital.appointment.repository;
//
//
//import com.hospital.smart_hospital.appointment.entity.Appointment;
//import com.hospital.smart_hospital.common.enums.AppointmentStatus;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface AppointmentRepository
//        extends JpaRepository<Appointment, Long> {
//
//    Optional<Appointment> findByAppointmentCode(String appointmentCode);
//
//    List<Appointment> findByPatientId(Long patientId);
//
//    List<Appointment> findByDoctorId(Long doctorId);
//
//    List<Appointment> findByStatus(AppointmentStatus status);
//
//    boolean existsByAppointmentSlotId(Long appointmentSlotId);
//
//}