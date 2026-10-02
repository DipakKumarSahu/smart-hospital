package com.hospital.smart_hospital.payment.controller;

import com.hospital.smart_hospital.common.dto.ApiResponse;
import com.hospital.smart_hospital.payment.dto.request.ConfirmPaymentRequest;
import com.hospital.smart_hospital.payment.dto.request.CreatePaymentRequest;
import com.hospital.smart_hospital.payment.dto.response.PaymentResponse;
import com.hospital.smart_hospital.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PaymentResponse> createPayment(
            @Valid @RequestBody CreatePaymentRequest request) {

        return ApiResponse.success(
                "Payment created successfully.",
                paymentService.createPayment(request)
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<PaymentResponse> getPaymentById(
            @PathVariable Long id) {

        return ApiResponse.success(
                "Payment fetched successfully.",
                paymentService.getPaymentById(id)
        );
    }

    @GetMapping("/appointment/{appointmentId}")
    public ApiResponse<PaymentResponse> getPaymentByAppointmentId(
            @PathVariable Long appointmentId) {

        return ApiResponse.success(
                "Payment fetched successfully.",
                paymentService.getPaymentByAppointmentId(
                        appointmentId
                )
        );
    }

    @PostMapping("/confirm")
    public ApiResponse<Void> confirmPayment(
            @Valid @RequestBody ConfirmPaymentRequest request) {

        paymentService.handleStripePaymentSuccess(
                request.paymentIntentId()
        );

        return ApiResponse.success(
                "Payment confirmed successfully.",
                null
        );
    }

}