package com.hospital.smart_hospital.department.repository;


import com.hospital.smart_hospital.department.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository
        extends JpaRepository<Department, Long> {

    Optional<Department> findByDepartmentCode(String code);

    Optional<Department> findByDepartmentName(String name);

    boolean existsByDepartmentCode(String code);
}
