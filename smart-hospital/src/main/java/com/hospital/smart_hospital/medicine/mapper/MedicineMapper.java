package com.hospital.smart_hospital.medicine.mapper;


import com.hospital.smart_hospital.medicine.dto.request.CreateMedicineRequest;
import com.hospital.smart_hospital.medicine.dto.request.UpdateMedicineRequest;
import com.hospital.smart_hospital.medicine.dto.response.MedicineResponse;
import com.hospital.smart_hospital.medicine.entity.Medicine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MedicineMapper {

//    @Mapping(target = "id", ignore = true)
    @Mapping(target = "medicineCode", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "prescriptionItems", ignore = true)
    Medicine toEntity(CreateMedicineRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "medicineCode", ignore = true)
    @Mapping(target = "prescriptionItems", ignore = true)
    void updateMedicine(UpdateMedicineRequest request,
                        @MappingTarget Medicine medicine);

    MedicineResponse toResponse(Medicine medicine);
}