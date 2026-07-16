package com.hospital.smart_hospital.staff.repository;



import com.hospital.smart_hospital.staff.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffRepository
        extends JpaRepository<Staff, Long> {

    Optional<Staff> findByEmployeeCode(String employeeCode);

    Optional<Staff> findByUserId(Long userId);

    boolean existsByEmployeeCode(String employeeCode);

    Optional<Staff> findByIdAndActiveTrue(Long id);
}
