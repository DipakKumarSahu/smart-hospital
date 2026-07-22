package com.hospital.smart_hospital.appointment.dto.request;



import com.hospital.smart_hospital.common.enums.BookingSource;
import com.hospital.smart_hospital.common.enums.VisitType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAppointmentRequest(

        @NotNull(message = "Patient is required.")
        Long patientId,

        @NotNull(message = "Doctor is required.")
        Long doctorId,

        @NotNull(message = "Appointment slot is required.")
        Long appointmentSlotId,

        @NotNull(message = "Visit type is required.")
        VisitType visitType,

        @NotNull(message = "Booking source is required.")
        BookingSource bookingSource,

        @NotBlank(message = "Reason for visit is required.")
        String reasonForVisit,

        String notes

) {
}