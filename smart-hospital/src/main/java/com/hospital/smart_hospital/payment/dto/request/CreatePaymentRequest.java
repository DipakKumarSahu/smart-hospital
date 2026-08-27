package com.hospital.smart_hospital.payment.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreatePaymentRequest(

        @NotNull(message = "Appointment id is required.")
        Long appointmentId

) {
}