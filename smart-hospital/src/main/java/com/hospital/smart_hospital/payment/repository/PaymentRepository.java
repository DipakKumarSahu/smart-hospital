package com.hospital.smart_hospital.payment.repository;

import com.hospital.smart_hospital.payment.entity.Payment;
import com.hospital.smart_hospital.payment.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    Optional<Payment> findByIdAndActiveTrue(Long id);

    Optional<Payment> findByPaymentCodeAndActiveTrue(
            String paymentCode
    );

    Optional<Payment> findByAppointmentIdAndActiveTrue(
            Long appointmentId
    );

    Optional<Payment> findByGatewayPaymentId(
            String gatewayPaymentId
    );

    Optional<Payment> findByGatewayOrderId(
            String gatewayOrderId
    );

    Page<Payment> findByStatusAndActiveTrue(
            PaymentStatus status,
            Pageable pageable
    );

    boolean existsByAppointmentIdAndActiveTrue(
            Long appointmentId
    );
}