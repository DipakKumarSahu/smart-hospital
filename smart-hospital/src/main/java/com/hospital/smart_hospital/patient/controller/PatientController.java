package com.hospital.smart_hospital.patient.controller;


import com.hospital.smart_hospital.common.dto.ApiResponse;
import com.hospital.smart_hospital.patient.dto.request.UpdateMyProfileRequest;
import com.hospital.smart_hospital.patient.dto.request.UpdatePatientRequest;
import com.hospital.smart_hospital.patient.dto.response.PatientResponse;
import com.hospital.smart_hospital.patient.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    // =====================================================
    // Admin APIs
    // =====================================================

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/patients/{id}")
    public ResponseEntity<ApiResponse<PatientResponse>> getPatientById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Patient fetched successfully.",
                        patientService.getPatientById(id)
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/patients")
    public ResponseEntity<ApiResponse<Page<PatientResponse>>> getPatients(
            @RequestParam(required = false) String keyword,
            Pageable pageable) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Patients fetched successfully.",
                        patientService.getPatients(keyword, pageable)
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/patients/{id}")
    public ResponseEntity<ApiResponse<PatientResponse>> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePatientRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Patient updated successfully.",
                        patientService.updatePatient(id, request)
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/patients/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePatient(
            @PathVariable Long id) {

        patientService.deletePatient(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Patient deleted successfully.",
                        null
                )
        );
    }

    // =====================================================
    // Patient Self APIs
    // =====================================================

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/patient/profile")
    public ResponseEntity<ApiResponse<PatientResponse>> getMyProfile() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Profile fetched successfully.",
                        patientService.getMyProfile()
                )
        );
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PutMapping("/patient/profile")
    public ResponseEntity<ApiResponse<PatientResponse>> updateMyProfile(
            @Valid @RequestBody UpdateMyProfileRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Profile updated successfully.",
                        patientService.updateMyProfile(request)
                )
        );
    }
}