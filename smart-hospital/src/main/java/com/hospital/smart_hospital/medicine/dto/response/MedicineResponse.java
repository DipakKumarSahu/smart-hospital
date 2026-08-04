package com.hospital.smart_hospital.medicine.dto.response;



import lombok.Builder;

@Builder
public record MedicineResponse(

        Long id,

        String medicineCode,

        String name,

        String genericName,

        String strength,

        String manufacturer,

        String dosageForm,

        boolean active

) {
}
