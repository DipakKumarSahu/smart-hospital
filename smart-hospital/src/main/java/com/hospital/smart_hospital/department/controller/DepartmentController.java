package com.hospital.smart_hospital.department.controller;

import com.hospital.smart_hospital.common.dto.ApiResponse;
import com.hospital.smart_hospital.department.dto.request.CreateDepartmentRequest;
import com.hospital.smart_hospital.department.dto.request.UpdateDepartmentRequest;
import com.hospital.smart_hospital.department.dto.response.DepartmentResponse;
import com.hospital.smart_hospital.department.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponse>> createDepartment(
            @Valid @RequestBody CreateDepartmentRequest request) {

        DepartmentResponse response =
                departmentService.createDepartment(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<DepartmentResponse>builder()
                        .success(true)
                        .message("Department created successfully.")
                        .data(response)
                        .build());
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST','PATIENT','LAB_TECHNICIAN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> getDepartmentById(
            @PathVariable Long id) {

        DepartmentResponse response =
                departmentService.getDepartmentById(id);

        return ResponseEntity.ok(
                ApiResponse.<DepartmentResponse>builder()
                        .success(true)
                        .message("Department fetched successfully.")
                        .data(response)
                        .build()
        );
    }


    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST','PATIENT','LAB_TECHNICIAN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getAllDepartments() {

        List<DepartmentResponse> response =
                departmentService.getAllDepartments();

        return ResponseEntity.ok(
                ApiResponse.<List<DepartmentResponse>>builder()
                        .success(true)
                        .message("Departments fetched successfully.")
                        .data(response)
                        .build()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDepartmentRequest request) {

        DepartmentResponse response =
                departmentService.updateDepartment(id, request);

        return ResponseEntity.ok(
                ApiResponse.<DepartmentResponse>builder()
                        .success(true)
                        .message("Department updated successfully.")
                        .data(response)
                        .build()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(
            @PathVariable Long id) {

        departmentService.deleteDepartment(id);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST','PATIENT','LAB_TECHNICIAN')")
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<Page<DepartmentResponse>>> getActiveDepartments(
            Pageable pageable) {

        Page<DepartmentResponse> response =
                departmentService.getAllActiveDepartments(pageable);

        return ResponseEntity.ok(
                ApiResponse.<Page<DepartmentResponse>>builder()
                        .success(true)
                        .message("Departments fetched successfully.")
                        .data(response)
                        .build()
        );
    }


    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST','PATIENT','LAB_TECHNICIAN')")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<DepartmentResponse>>> searchDepartments(
            @RequestParam String keyword,
            Pageable pageable) {

        Page<DepartmentResponse> response =
                departmentService.searchDepartments(
                        keyword,
                        pageable
                );

        return ResponseEntity.ok(
                ApiResponse.<Page<DepartmentResponse>>builder()
                        .success(true)
                        .message("Departments fetched successfully.")
                        .data(response)
                        .build()
        );
    }
}
