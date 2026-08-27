package com.hospital.smart_hospital.payment.service;





import com.hospital.smart_hospital.payment.dto.response.PaymentGatewayResponse;

import java.math.BigDecimal;

public interface PaymentGateway {

    PaymentGatewayResponse createPayment(
            BigDecimal amount,
            String currency,
            String paymentReference
    );
}
