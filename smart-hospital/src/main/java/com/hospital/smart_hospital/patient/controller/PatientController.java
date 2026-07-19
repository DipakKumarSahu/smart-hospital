package com.hospital.smart_hospital.patient.controller;


import com.hospital.smart_hospital.common.dto.ApiResponse;
import com.hospital.smart_hospital.patient.dto.request.CreatePatientRequest;
import com.hospital.smart_hospital.patient.dto.request.UpdatePatientRequest;
import com.hospital.smart_hospital.patient.dto.response.PatientResponse;
import com.hospital.smart_hospital.patient.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<ApiResponse<PatientResponse>> createPatient(
            @Valid @RequestBody CreatePatientRequest request) {

        PatientResponse response = patientService.createPatient(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<PatientResponse>builder()
                        .success(true)
                        .message("Patient created successfully.")
                        .data(response)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientResponse>> getPatientById(
            @PathVariable Long id) {

        PatientResponse response = patientService.getPatientById(id);

        return ResponseEntity.ok(
                ApiResponse.<PatientResponse>builder()
                        .success(true)
                        .message("Patient fetched successfully.")
                        .data(response)
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PatientResponse>>> getPatients(
            @RequestParam(required = false) String keyword,
            Pageable pageable) {

        Page<PatientResponse> response =
                patientService.getPatients(keyword, pageable);

        return ResponseEntity.ok(
                ApiResponse.<Page<PatientResponse>>builder()
                        .success(true)
                        .message("Patients fetched successfully.")
                        .data(response)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientResponse>> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePatientRequest request) {

        PatientResponse response =
                patientService.updatePatient(id, request);

        return ResponseEntity.ok(
                ApiResponse.<PatientResponse>builder()
                        .success(true)
                        .message("Patient updated successfully.")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePatient(
            @PathVariable Long id) {

        patientService.deletePatient(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Patient deleted successfully.")
                        .build()
        );
    }
}
