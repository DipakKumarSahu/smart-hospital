package com.hospital.smart_hospital.medicine.controller;



import com.hospital.smart_hospital.common.dto.ApiResponse;
import com.hospital.smart_hospital.medicine.dto.request.CreateMedicineRequest;
import com.hospital.smart_hospital.medicine.dto.request.UpdateMedicineRequest;
import com.hospital.smart_hospital.medicine.dto.response.MedicineResponse;
import com.hospital.smart_hospital.medicine.service.MedicineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/medicines")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<MedicineResponse>> createMedicine(
            @Valid @RequestBody CreateMedicineRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Medicine created successfully.",
                        medicineService.createMedicine(request)
                ));
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST','PATIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicineResponse>> getMedicineById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Medicine fetched successfully.",
                        medicineService.getMedicineById(id)
                )
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST','PATIENT')")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<MedicineResponse>>> getMedicines(
            @RequestParam(required = false) String keyword,
            Pageable pageable) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Medicines fetched successfully.",
                        medicineService.getMedicines(keyword, pageable)
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicineResponse>> updateMedicine(
            @PathVariable Long id,
            @Valid @RequestBody UpdateMedicineRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Medicine updated successfully.",
                        medicineService.updateMedicine(id, request)
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMedicine(
            @PathVariable Long id) {

        medicineService.deleteMedicine(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Medicine deleted successfully.",
                        null
                )
        );
    }
}