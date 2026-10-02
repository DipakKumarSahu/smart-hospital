package com.hospital.smart_hospital.doctor.repository;

import com.hospital.smart_hospital.doctor.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByDoctorCode(String doctorCode);

    Optional<Doctor> findByIdAndActiveTrue(Long id);

    boolean existsByLicenseNumberIgnoreCase(String licenseNumber);

    boolean existsByStaffId(Long staffId);

    Page<Doctor> findByActiveTrue(Pageable pageable);

    Page<Doctor> findBySpecializationContainingIgnoreCaseAndActiveTrue(
            String specialization,
            Pageable pageable
    );

//    Page<Doctor> findByActiveTrueAndStaffUserFullNameContainingIgnoreCaseOrActiveTrueAndSpecializationContainingIgnoreCase(
//            String fullName,
//            String specialization,
//            Pageable pageable
//    );

    Page<Doctor> findByStaffUserFullNameContainingIgnoreCaseAndActiveTrue(
            String fullName,
            Pageable pageable
    );

    Optional<Doctor> findByStaffIdAndActiveTrue(Long staffId);

    List<Doctor> findByDepartmentDepartmentCodeIgnoreCaseAndActiveTrue(
            String departmentCode
    );

    List<Doctor> findByDepartmentIdAndActiveTrue(Long departmentId);
}