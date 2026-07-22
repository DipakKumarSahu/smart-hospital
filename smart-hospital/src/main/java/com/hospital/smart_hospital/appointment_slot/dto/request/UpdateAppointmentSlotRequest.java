package com.hospital.smart_hospital.appointment_slot.dto.request;



import com.hospital.smart_hospital.common.enums.SlotStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateAppointmentSlotRequest(

        @NotNull(message = "Status is required.")
        SlotStatus status,

        Boolean active

) {
}