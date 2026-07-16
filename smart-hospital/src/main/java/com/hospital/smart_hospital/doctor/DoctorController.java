package com.hospital.smart_hospital.doctor;

import com.hospital.smart_hospital.common.dto.ApiResponse;
import com.hospital.smart_hospital.doctor.dto.request.CreateDoctorRequest;
import com.hospital.smart_hospital.doctor.dto.request.UpdateDoctorRequest;
import com.hospital.smart_hospital.doctor.dto.response.DoctorResponse;
import com.hospital.smart_hospital.doctor.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<ApiResponse<DoctorResponse>> createDoctor(
            @Valid @RequestBody CreateDoctorRequest request) {

        DoctorResponse response =
                doctorService.createDoctor(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<DoctorResponse>builder()
                        .success(true)
                        .message("Doctor created successfully.")
                        .data(response)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DoctorResponse>> getDoctorById(
            @PathVariable Long id) {

        DoctorResponse response =
                doctorService.getDoctorById(id);

        return ResponseEntity.ok(
                ApiResponse.<DoctorResponse>builder()
                        .success(true)
                        .message("Doctor fetched successfully.")
                        .data(response)
                        .build()
        );
    }


//    @GetMapping
//    public ResponseEntity<ApiResponse<Page<DoctorResponse>>> getAllDoctors(
//            Pageable pageable) {
//
//        Page<DoctorResponse> response =
//                doctorService.getDoctors(pageable);
//
//        return ResponseEntity.ok(
//                ApiResponse.<Page<DoctorResponse>>builder()
//                        .success(true)
//                        .message("Doctors fetched successfully.")
//                        .data(response)
//                        .build()
//        );
//    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DoctorResponse>> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDoctorRequest request) {

        DoctorResponse response =
                doctorService.updateDoctor(id, request);

        return ResponseEntity.ok(
                ApiResponse.<DoctorResponse>builder()
                        .success(true)
                        .message("Doctor updated successfully.")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(
            @PathVariable Long id) {

        doctorService.deleteDoctor(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<DoctorResponse>>> getDoctors(

            @RequestParam(required = false) String keyword,
            Pageable pageable) {

        Page<DoctorResponse> response =
                doctorService.getDoctors(keyword, pageable);

        return ResponseEntity.ok(
                ApiResponse.<Page<DoctorResponse>>builder()
                        .success(true)
                        .message("Doctors fetched successfully.")
                        .data(response)
                        .build()
        );
    }
}