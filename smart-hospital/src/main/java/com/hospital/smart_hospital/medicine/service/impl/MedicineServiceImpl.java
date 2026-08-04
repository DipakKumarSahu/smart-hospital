package com.hospital.smart_hospital.medicine.service.impl;

import com.hospital.smart_hospital.exception.BadRequestException;
import com.hospital.smart_hospital.exception.ResourceNotFoundException;
import com.hospital.smart_hospital.medicine.dto.request.CreateMedicineRequest;
import com.hospital.smart_hospital.medicine.dto.request.UpdateMedicineRequest;
import com.hospital.smart_hospital.medicine.dto.response.MedicineResponse;
import com.hospital.smart_hospital.medicine.entity.Medicine;
import com.hospital.smart_hospital.medicine.mapper.MedicineMapper;
import com.hospital.smart_hospital.medicine.repository.MedicineRepository;
import com.hospital.smart_hospital.medicine.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;
    private final MedicineMapper mapper;

    @Override
    public MedicineResponse createMedicine(CreateMedicineRequest request) {

        validateCreateMedicine(request);

        Medicine medicine = mapper.toEntity(request);

        medicine.setMedicineCode(generateMedicineCode());
        medicine.setActive(true);

        medicine = medicineRepository.save(medicine);

        return mapper.toResponse(medicine);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicineResponse getMedicineById(Long id) {

        return mapper.toResponse(getMedicine(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MedicineResponse> getMedicines(String keyword,
                                               Pageable pageable) {

        if (keyword == null || keyword.isBlank()) {

            return medicineRepository
                    .findByActiveTrue(pageable)
                    .map(mapper::toResponse);
        }

        return medicineRepository
                .findByNameContainingIgnoreCaseAndActiveTrue(
                        keyword,
                        pageable
                )
                .map(mapper::toResponse);
    }

    @Override
    public MedicineResponse updateMedicine(Long id,
                                           UpdateMedicineRequest request) {

        validateUpdateMedicine(id, request);

        Medicine medicine = getMedicine(id);

        mapper.updateMedicine(request, medicine);

        medicine = medicineRepository.save(medicine);

        return mapper.toResponse(medicine);
    }

    @Override
    public void deleteMedicine(Long id) {

        Medicine medicine = getMedicine(id);

        medicine.setActive(false);

        medicineRepository.save(medicine);
    }

    // =====================================================
    // Helper Methods
    // =====================================================

    private Medicine getMedicine(Long id) {

        return medicineRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Medicine not found."
                        ));
    }

    private void validateCreateMedicine(CreateMedicineRequest request) {

        if (medicineRepository
                .existsByNameIgnoreCaseAndStrengthIgnoreCase(
                        request.name(),
                        request.strength()
                )) {

            throw new BadRequestException(
                    "Medicine already exists."
            );
        }
    }

    private void validateUpdateMedicine(Long id,
                                        UpdateMedicineRequest request) {

        if (medicineRepository
                .existsByNameIgnoreCaseAndStrengthIgnoreCaseAndIdNot(
                        request.name(),
                        request.strength(),
                        id
                )) {

            throw new BadRequestException(
                    "Medicine already exists."
            );
        }
    }

    private String generateMedicineCode() {

        long count = medicineRepository.count() + 1;

        return String.format("MED-%05d", count);
    }
}