package com.hospital.smart_hospital.patient.repository;


import com.hospital.smart_hospital.patient.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByPatientCode(String patientCode);

    Optional<Patient> findByIdAndActiveTrue(Long id);

    boolean existsByUserId(Long userId);

    Page<Patient> findByActiveTrue(Pageable pageable);

    Page<Patient> findByUserFullNameContainingIgnoreCaseAndActiveTrue(
            String fullName,
            Pageable pageable
    );
}