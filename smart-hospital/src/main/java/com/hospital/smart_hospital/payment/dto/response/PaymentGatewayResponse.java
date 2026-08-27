package com.hospital.smart_hospital.payment.dto.response;


public record PaymentGatewayResponse(

        String paymentId,

        String clientSecret

) {
}
