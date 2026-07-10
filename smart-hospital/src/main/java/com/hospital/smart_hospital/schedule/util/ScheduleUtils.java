package com.hospital.smart_hospital.schedule.util;

import java.time.LocalTime;

public final class ScheduleUtils {

    private ScheduleUtils() {
    }

    public static boolean isBreakTime(
            LocalTime current,
            LocalTime breakStart,
            LocalTime breakEnd
    ) {

        if (breakStart == null || breakEnd == null) {
            return false;
        }

        return !current.isBefore(breakStart)
                && current.isBefore(breakEnd);
    }
}
