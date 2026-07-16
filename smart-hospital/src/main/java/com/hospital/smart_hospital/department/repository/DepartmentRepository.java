package com.hospital.smart_hospital.department.repository;


import com.hospital.smart_hospital.department.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentRepository
        extends JpaRepository<Department, Long> {

    Optional<Department> findByDepartmentCode(String code);

    Optional<Department> findByDepartmentName(String name);

    boolean existsByDepartmentCodeIgnoreCase(String departmentCode);

    boolean existsByDepartmentNameIgnoreCase(String departmentName);

    List<Department> findByActiveTrue();

    Page<Department> findByActiveTrue(Pageable pageable);

    Page<Department> findByDepartmentNameContainingIgnoreCaseAndActiveTrue(
            String keyword,
            Pageable pageable
    );
}
