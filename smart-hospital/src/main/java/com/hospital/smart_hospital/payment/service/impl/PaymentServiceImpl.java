package com.hospital.smart_hospital.payment.service.impl;

import com.hospital.smart_hospital.appointment.entity.Appointment;
import com.hospital.smart_hospital.appointment.repository.AppointmentRepository;
import com.hospital.smart_hospital.appointment_slot.entity.AppointmentSlot;
import com.hospital.smart_hospital.appointment_slot.repository.AppointmentSlotRepository;
import com.hospital.smart_hospital.common.enums.AppointmentStatus;
import com.hospital.smart_hospital.common.enums.SlotStatus;
import com.hospital.smart_hospital.doctor.entity.Doctor;
import com.hospital.smart_hospital.payment.dto.request.CreatePaymentRequest;
import com.hospital.smart_hospital.payment.dto.response.PaymentGatewayResponse;
import com.hospital.smart_hospital.payment.dto.response.PaymentResponse;
import com.hospital.smart_hospital.payment.entity.Payment;
import com.hospital.smart_hospital.payment.enums.PaymentStatus;
import com.hospital.smart_hospital.payment.repository.PaymentRepository;
import com.hospital.smart_hospital.payment.service.PaymentGateway;
import com.hospital.smart_hospital.payment.service.PaymentService;
import com.hospital.smart_hospital.exception.BadRequestException;
import com.hospital.smart_hospital.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private static final String GATEWAY = "STRIPE";
    private static final String CURRENCY = "INR";

    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentSlotRepository slotRepository;
    private final PaymentGateway paymentGateway;

    @Override
    public PaymentResponse createPayment(
            CreatePaymentRequest request) {

        Appointment appointment =
                appointmentRepository
                        .findByIdAndActiveTrue(
                                request.appointmentId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Appointment not found."
                                ));
        log.error("Appointment not found.");

        validateAppointmentForPayment(appointment);

        if (paymentRepository
                .existsByAppointmentIdAndActiveTrue(
                        appointment.getId())) {

            log.error("Payment already exists for this appointment.");
            throw new BadRequestException(
                    "Payment already exists for this appointment."
            );


        }

        BigDecimal amount =
                appointment
                        .getDoctor()
                        .getConsultationFee();

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new BadRequestException(
                    "Invalid consultation fee."
            );
        }

        Payment payment = Payment.builder()
                .paymentCode(generatePaymentCode())
                .appointment(appointment)
                .amount(amount)
                .currency("INR")
                .status(PaymentStatus.PENDING)
                .gateway("STRIPE")
                .active(true)
                .build();

        Payment savedPayment =
                paymentRepository.save(payment);

        PaymentGatewayResponse gatewayResponse =
                paymentGateway.createPayment(
                        savedPayment.getAmount(),
                        savedPayment.getCurrency(),
                        savedPayment.getPaymentCode()
                );

        savedPayment.setGatewayPaymentId(
                gatewayResponse.paymentId()
        );

        savedPayment =
                paymentRepository.save(savedPayment);

        return new PaymentResponse(
                savedPayment.getId(),
                savedPayment.getPaymentCode(),
                savedPayment.getAppointment().getId(),
                savedPayment.getAmount(),
                savedPayment.getCurrency(),
                savedPayment.getStatus(),
                savedPayment.getGateway(),
                savedPayment.getGatewayPaymentId(),
//                savedPayment.getGatewayOrderId(),
                savedPayment.getPaidAt(),
                savedPayment.getFailureReason(),
                gatewayResponse.clientSecret()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Long id) {

        Payment payment =
                paymentRepository
                        .findByIdAndActiveTrue(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment not found."
                                ));

        return toResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByAppointmentId(
            Long appointmentId) {

        Payment payment =
                paymentRepository
                        .findByAppointmentIdAndActiveTrue(
                                appointmentId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment not found."
                                ));

        return toResponse(payment);
    }

    // =========================================================
    // Helper Methods
    // =========================================================

    private void validateAppointmentForPayment(
            Appointment appointment) {

        if (appointment.getAppointmentStatus()
                != AppointmentStatus.PAYMENT_PENDING) {

            throw new BadRequestException(
                    "Appointment is not waiting for payment."
            );
        }
    }

    private String generatePaymentCode() {

        long count =
                paymentRepository.count() + 1;

        return String.format(
                "PAY-%06d",
                count
        );
    }

    private PaymentResponse toResponse(
            Payment payment) {

        return new PaymentResponse(
                payment.getId(),
                payment.getPaymentCode(),
                payment.getAppointment().getId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getStatus(),
                payment.getGateway(),
                payment.getGatewayPaymentId(),
//                payment.getGatewayOrderId(),
                payment.getPaidAt(),
                payment.getFailureReason(),
                null
        );
    }

    @Override
    public void handleStripePaymentSuccess(
            String paymentIntentId) {

        log.info(
                "Processing successful payment: {}",
                paymentIntentId
        );

        Payment payment =
                paymentRepository
                        .findByGatewayPaymentId(paymentIntentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment not found for PaymentIntent: "
                                                + paymentIntentId
                                )
                        );

        if (payment.getStatus() == PaymentStatus.SUCCESS) {

            log.info(
                    "Payment already processed: {}",
                    paymentIntentId
            );

            return;
        }

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());

        Appointment appointment =
                payment.getAppointment();

        appointment.setAppointmentStatus(
                AppointmentStatus.BOOKED
        );

        AppointmentSlot slot =
                appointment.getAppointmentSlot();

        slot.setStatus(SlotStatus.BOOKED);

        paymentRepository.save(payment);
        appointmentRepository.save(appointment);
        slotRepository.save(slot);

        log.info(
                "Payment successful. paymentId={}, appointmentId={}, slotId={}",
                payment.getId(),
                appointment.getId(),
                slot.getId()
        );
    }

    @Override
    public void handleStripePaymentFailure(
            String paymentIntentId,
            String failureReason) {

        Payment payment =
                paymentRepository
                        .findByGatewayPaymentId(paymentIntentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment not found for PaymentIntent: "
                                                + paymentIntentId
                                )
                        );

        if (payment.getStatus() == PaymentStatus.FAILED) {
            return;
        }

        payment.setStatus(PaymentStatus.FAILED);
        payment.setFailureReason(failureReason);

        Appointment appointment =
                payment.getAppointment();

        appointment.setAppointmentStatus(
                AppointmentStatus.CANCELLED
        );

        AppointmentSlot slot =
                appointment.getAppointmentSlot();

        slot.setStatus(SlotStatus.AVAILABLE);

        paymentRepository.save(payment);
        appointmentRepository.save(appointment);
        slotRepository.save(slot);
    }

}