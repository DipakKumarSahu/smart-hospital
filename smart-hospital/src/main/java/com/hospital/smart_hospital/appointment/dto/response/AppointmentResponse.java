package com.hospital.smart_hospital.appointment.dto.response;


import com.hospital.smart_hospital.common.enums.AppointmentStatus;
import com.hospital.smart_hospital.common.enums.BookingSource;
import com.hospital.smart_hospital.common.enums.PaymentStatus;
import com.hospital.smart_hospital.common.enums.VisitType;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record AppointmentResponse(

        Long id,

        String appointmentCode,

        Long patientId,

        String patientName,

        Long doctorId,

        String doctorCode,

        String doctorName,

        Long appointmentSlotId,

        LocalDate appointmentDate,

        LocalTime appointmentTime,

        VisitType visitType,

        BookingSource bookingSource,

        AppointmentStatus status,

        PaymentStatus paymentStatus,

        String reasonForVisit,

        String notes,

        boolean active

) {
}
