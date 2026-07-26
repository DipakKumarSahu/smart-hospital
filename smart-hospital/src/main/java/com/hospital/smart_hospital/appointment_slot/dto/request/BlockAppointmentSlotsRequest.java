package com.hospital.smart_hospital.appointment_slot.dto.request;


import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record BlockAppointmentSlotsRequest(

        @NotNull(message = "Doctor id is required.")
        Long doctorId,

        @NotNull(message = "Slot date is required.")
        LocalDate slotDate,

        @NotNull(message = "Start time is required.")
        LocalTime startTime,

        @NotNull(message = "End time is required.")
        LocalTime endTime
) {
}