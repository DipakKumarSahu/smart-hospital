package com.hospital.smart_hospital.payment.entity;



import com.hospital.smart_hospital.appointment.entity.Appointment;
import com.hospital.smart_hospital.common.entity.AuditableEntity;
import com.hospital.smart_hospital.payment.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "payments",
        indexes = {
                @Index(
                        name = "idx_payment_appointment",
                        columnList = "appointment_id"
                ),
                @Index(
                        name = "idx_payment_status",
                        columnList = "status"
                ),
                @Index(
                        name = "idx_gateway_payment_id",
                        columnList = "gateway_payment_id"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends AuditableEntity {

    @Column(
            name = "payment_code",
            nullable = false,
            unique = true,
            length = 30
    )
    private String paymentCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "appointment_id",
            nullable = false,
            unique = true
    )
    private Appointment appointment;

    @Column(
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal amount;

    @Column(
            nullable = false,
            length = 3
    )
    private String currency = "INR";

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 30
    )
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(
            name = "gateway",
            nullable = false,
            length = 30
    )
    private String gateway;

    @Column(
            name = "gateway_payment_id",
            unique = true,
            length = 255
    )
    private String gatewayPaymentId;

    @Column(
            name = "gateway_order_id",
            unique = true,
            length = 255
    )
    private String gatewayOrderId;

    @Column(
            name = "paid_at"
    )
    private LocalDateTime paidAt;

    @Column(
            name = "failure_reason",
            length = 500
    )
    private String failureReason;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;
}