package com.hospital.smart_hospital.payment.service;

import com.hospital.smart_hospital.payment.dto.request.CreatePaymentRequest;
import com.hospital.smart_hospital.payment.dto.response.PaymentResponse;

public interface PaymentService {

    PaymentResponse createPayment(
            CreatePaymentRequest request
    );

    PaymentResponse getPaymentById(
            Long id
    );

    PaymentResponse getPaymentByAppointmentId(
            Long appointmentId
    );

    void handleStripePaymentSuccess(
            String paymentIntentId
    );

    void handleStripePaymentFailure(
            String paymentIntentId,
            String failureReason
    );
}