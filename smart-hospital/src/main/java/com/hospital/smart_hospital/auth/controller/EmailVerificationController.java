package com.hospital.smart_hospital.auth.controller;



import com.hospital.smart_hospital.auth.service.EmailVerificationService;
import com.hospital.smart_hospital.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @GetMapping("/verify-email")
    public ApiResponse<Void> verifyEmail(
            @RequestParam String token) {

        emailVerificationService.verifyEmail(token);

        return ApiResponse.success(
                "Email verified successfully.",
                null
        );
    }
}
