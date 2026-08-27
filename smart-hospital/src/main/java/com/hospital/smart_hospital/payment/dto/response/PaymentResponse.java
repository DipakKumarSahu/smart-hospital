package com.hospital.smart_hospital.payment.dto.response;

import com.hospital.smart_hospital.payment.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(

        Long id,

        String paymentCode,

        Long appointmentId,

        BigDecimal amount,

        String currency,

        PaymentStatus status,

        String gateway,

        String gatewayPaymentId,

//        String gatewayOrderId,

        LocalDateTime paidAt,

        String failureReason,

        String clientSecret

) {
}