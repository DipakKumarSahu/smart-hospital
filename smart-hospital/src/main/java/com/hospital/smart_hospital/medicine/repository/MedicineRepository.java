package com.hospital.smart_hospital.medicine.repository;



import com.hospital.smart_hospital.medicine.entity.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicineRepository
        extends JpaRepository<Medicine, Long> {

    Optional<Medicine> findByIdAndActiveTrue(Long id);

    Optional<Medicine> findByMedicineCode(String medicineCode);

    Page<Medicine> findByActiveTrue(Pageable pageable);

    Page<Medicine> findByNameContainingIgnoreCaseAndActiveTrue(
            String keyword,
            Pageable pageable
    );

    boolean existsByNameIgnoreCaseAndStrengthIgnoreCase(
            String name,
            String strength
    );

    boolean existsByNameIgnoreCaseAndStrengthIgnoreCaseAndIdNot(
            String name,
            String strength,
            Long id
    );
}