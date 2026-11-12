package com.hospital.smart_hospital.staff.repository;

import com.hospital.smart_hospital.staff.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    Optional<Staff> findByEmployeeCode(String employeeCode);
    Optional<Staff> findByUserId(Long userId);
    boolean existsByEmployeeCode(String employeeCode);
    Optional<Staff> findByIdAndActiveTrue(Long id);
    Optional<Staff> findByUserIdAndActiveTrue(Long userId);

    // 👇 Custom Query for fetching Staff with User details (to avoid N+1)
    @Query("SELECT s FROM Staff s JOIN FETCH s.user u WHERE " +
            "(:keyword IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(s.employeeCode) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Staff> searchStaff(@Param("keyword") String keyword, Pageable pageable);

    // Count for Dashboard
    long countByActiveTrue();
    long countByActiveFalse();
}