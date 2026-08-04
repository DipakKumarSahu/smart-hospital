package com.hospital.smart_hospital.medicine.dto.request;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateMedicineRequest(

        @NotBlank(message = "Medicine name is required.")
        @Size(max = 150)
        String name,

        @Size(max = 150)
        String genericName,

        @Size(max = 50)
        String strength,

        @Size(max = 100)
        String manufacturer,

        @Size(max = 100)
        String dosageForm

) {
}