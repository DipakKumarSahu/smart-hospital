package com.hospital.smart_hospital.medicine.service;

import com.hospital.smart_hospital.medicine.dto.request.CreateMedicineRequest;
import com.hospital.smart_hospital.medicine.dto.request.UpdateMedicineRequest;
import com.hospital.smart_hospital.medicine.dto.response.MedicineResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MedicineService {

    MedicineResponse createMedicine(CreateMedicineRequest request);

    MedicineResponse getMedicineById(Long id);

    Page<MedicineResponse> getMedicines(
            String keyword,
            Pageable pageable
    );

    MedicineResponse updateMedicine(
            Long id,
            UpdateMedicineRequest request
    );

    void deleteMedicine(Long id);
}