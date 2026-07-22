package com.hospital.smart_hospital.schedule.dto.request;



import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record UpdateDoctorScheduleRequest(

        @NotNull
        DayOfWeek dayOfWeek,

        @NotNull
        LocalTime startTime,

        @NotNull
        LocalTime endTime,

        @NotNull
        @Min(5)
        Integer slotDurationMinutes,

        Boolean active

) {
}
