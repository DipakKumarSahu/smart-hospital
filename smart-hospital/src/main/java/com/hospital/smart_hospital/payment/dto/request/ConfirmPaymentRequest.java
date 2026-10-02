package com.hospital.smart_hospital.payment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ConfirmPaymentRequest(

        @NotBlank(message = "Payment intent id is required.")
        String paymentIntentId

) {
}