package com.hospital.smart_hospital.security.controller;



import com.hospital.smart_hospital.auth.dto.request.CreateDoctorByAdminRequest;
import com.hospital.smart_hospital.auth.dto.request.RegisterPatientRequest;
import com.hospital.smart_hospital.common.dto.ApiResponse;
import com.hospital.smart_hospital.auth.dto.request.LoginRequest;
import com.hospital.smart_hospital.auth.dto.response.LoginResponse;
import com.hospital.smart_hospital.auth.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = authenticationService.login(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Login successful.",
                        response
                )
        );
    }

    @PostMapping("/register/patient")
    public ResponseEntity<ApiResponse<Void>> registerPatient(
            @Valid @RequestBody RegisterPatientRequest request) {

        authenticationService.registerPatient(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Patient registered successfully.",
                                null
                        )
                );
    }

    @PostMapping("/admin/register-doctor")
    @PreAuthorize("hasRole('ADMIN')") // 👈 Sirf Admin hi access kar sakta hai
    public ResponseEntity<ApiResponse<Void>> registerDoctorByAdmin(
            @Valid @RequestBody CreateDoctorByAdminRequest request) {

        authenticationService.createDoctorByAdmin(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Doctor registered successfully and email sent.", null));
    }

}
