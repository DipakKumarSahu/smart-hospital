package com.hospital.smart_hospital.doctor.repository;


import com.hospital.smart_hospital.doctor.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByDoctorCode(String doctorCode);

    Optional<Doctor> findByLicenseNumber(String licenseNumber);

    Optional<Doctor> findByStaffId(Long staffId);

    List<Doctor> findByDepartmentId(Long departmentId);

    boolean existsByDoctorCode(String doctorCode);

    boolean existsByLicenseNumber(String licenseNumber);
}