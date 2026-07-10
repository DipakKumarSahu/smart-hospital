package com.hospital.smart_hospital.schedule.impl;

import com.hospital.smart_hospital.schedule.enitiy.AppointmentSlot;
import com.hospital.smart_hospital.schedule.enitiy.DoctorSchedule;
import com.hospital.smart_hospital.schedule.repository.AppointmentSlotRepository;
import com.hospital.smart_hospital.schedule.repository.DoctorScheduleRepository;
import com.hospital.smart_hospital.schedule.service.DoctorScheduleService;
import com.hospital.smart_hospital.schedule.util.ScheduleUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorScheduleServiceImpl
        implements DoctorScheduleService {

    private final DoctorScheduleRepository doctorScheduleRepository;

    private final AppointmentSlotRepository appointmentSlotRepository;


    @Override
    public int generateSlots(Long doctorId,
                                               LocalDate date) {

        validateDate(date);

        DoctorSchedule schedule =
                getDoctorSchedule(doctorId, date);

        validateDuplicateSlots(schedule, date);



        return generateAppointmentSlots(schedule,date);
    }



    private void validateDate(LocalDate date) {

        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Cannot generate slots for past dates."
            );
        }

    }

    private DoctorSchedule getDoctorSchedule(
            Long doctorId,
            LocalDate date
    ) {

        DayOfWeek day = date.getDayOfWeek();

        return doctorScheduleRepository
                .findByDoctorIdAndDayOfWeekAndActiveTrue(
                        doctorId,
                        day
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Doctor schedule not found."
                        ));

    }

    private void validateDuplicateSlots(
            DoctorSchedule schedule,
            LocalDate date
    ) {

        boolean exists =
                appointmentSlotRepository
                        .existsByDoctorScheduleIdAndSlotDate(
                                schedule.getId(),
                                date
                        );

        if (exists) {
            throw new RuntimeException(
                    "Slots already generated."
            );
        }

    }

    private int generateAppointmentSlots(
            DoctorSchedule schedule,
            LocalDate date
    ) {

        LocalTime current = schedule.getStartTime();

        List<AppointmentSlot> slots = new ArrayList<>();

        while (!current.plusMinutes(schedule.getSlotDurationMinutes())
                .isAfter(schedule.getEndTime())) {

            if (ScheduleUtils.isBreakTime(
                    current,
                    schedule.getBreakStartTime(),
                    schedule.getBreakEndTime())) {

                current = schedule.getBreakEndTime();
                continue;
            }

            slots.add(buildSlot(schedule, date, current));

            current = current.plusMinutes(
                    schedule.getSlotDurationMinutes()
            );
        }

        appointmentSlotRepository.saveAll(slots);

        return slots.size();
    }

    private void createSlot(
            DoctorSchedule schedule,
            LocalDate date,
            LocalTime startTime
    ) {

        AppointmentSlot slot =
                AppointmentSlot.builder()
                        .doctorSchedule(schedule)
                        .slotDate(date)
                        .startTime(startTime)
                        .endTime(
                                startTime.plusMinutes(
                                        schedule.getSlotDurationMinutes()
                                )
                        )
                        .build();

        appointmentSlotRepository.save(slot);

    }

    private AppointmentSlot buildSlot(
            DoctorSchedule schedule,
            LocalDate date,
            LocalTime startTime
    ) {

        return AppointmentSlot.builder()
                .doctorSchedule(schedule)
                .slotDate(date)
                .startTime(startTime)
                .endTime(
                        startTime.plusMinutes(
                                schedule.getSlotDurationMinutes()
                        )
                )
                .build();
    }

}
