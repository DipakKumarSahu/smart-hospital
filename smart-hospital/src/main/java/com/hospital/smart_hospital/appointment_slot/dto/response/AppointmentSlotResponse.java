package com.hospital.smart_hospital.appointment_slot.dto.response;


import com.hospital.smart_hospital.common.enums.SlotStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record AppointmentSlotResponse(

        Long id,

        Long doctorId,

        String doctorCode,

        String doctorName,

        Long doctorScheduleId,

        LocalDate slotDate,

        LocalTime startTime,

        LocalTime endTime,

        SlotStatus status,

        boolean active

) {
}
