package com.hospital.smart_hospital.auth.controller;



import com.hospital.smart_hospital.auth.service.EmailVerificationService;
import com.hospital.smart_hospital.common.dto.ApiResponse;
import com.hospital.smart_hospital.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @Value("${app.frontend.url}")
    private String frontendUrl;


//    @GetMapping("/verify-email")
//    public ApiResponse<Void> verifyEmail(
//            @RequestParam String token) {
//
//        emailVerificationService.verifyEmail(token);
//
//        return ApiResponse.success(
//                "Email verified successfully.",
//                null
//        );
//    }
//}

    @GetMapping("/verify-email")
    public ResponseEntity<Void> verifyEmail(@RequestParam String token) {
        try {
            emailVerificationService.verifyEmail(token);

            // Success — redirect to frontend with success status
            String redirectUrl = frontendUrl + "/login?verified=true";
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(redirectUrl))
                    .build();

        } catch (BadRequestException e) {
            // Failure — redirect with error
            String redirectUrl = frontendUrl + "/login?verified=false&reason="
                    + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(redirectUrl))
                    .build();
        }
    }
}

