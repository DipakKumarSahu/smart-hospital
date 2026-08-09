package com.hospital.smart_hospital.appointment.dto.request;


import com.hospital.smart_hospital.common.enums.VisitType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PatientBookAppointmentRequest(

        @NotNull(message = "Doctor is required.")
        Long doctorId,

        @NotNull(message = "Appointment slot is required.")
        Long appointmentSlotId,

        @NotNull(message = "Visit type is required.")
        VisitType visitType,

        @NotBlank(message = "Reason for visit is required.")
        @Size(max = 500, message = "Reason for visit cannot exceed 500 characters.")
        String reasonForVisit

) {
}