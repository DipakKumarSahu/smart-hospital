package com.hospital.smart_hospital.appointment.dto.request;


import com.hospital.smart_hospital.common.enums.AppointmentStatus;
import com.hospital.smart_hospital.common.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateAppointmentRequest(

        @NotNull(message = "Appointment status is required.")
        AppointmentStatus status,

        @NotNull(message = "Payment status is required.")
        PaymentStatus paymentStatus,

        String notes,

        Boolean active

) {
}
