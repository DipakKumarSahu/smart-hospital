package com.hospital.smart_hospital.schedule.dto.response;


import lombok.Builder;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Builder
public record DoctorScheduleResponse(

        Long id,

        Long doctorId,

        String doctorCode,

        String doctorName,

        DayOfWeek dayOfWeek,

        LocalTime startTime,

        LocalTime endTime,

        Integer slotDurationMinutes,

        boolean active

) {
}