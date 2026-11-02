package com.hospital.smart_hospital.schedule.service.impl;


import com.hospital.smart_hospital.appointment_slot.repository.AppointmentSlotRepository;
import com.hospital.smart_hospital.appointment_slot.service.AppointmentSlotService;

import com.hospital.smart_hospital.doctor.entity.Doctor;
import com.hospital.smart_hospital.doctor.repository.DoctorRepository;
import com.hospital.smart_hospital.exception.BadRequestException;
import com.hospital.smart_hospital.exception.ResourceNotFoundException;

import com.hospital.smart_hospital.schedule.dto.request.CreateDoctorScheduleRequest;
import com.hospital.smart_hospital.schedule.dto.request.UpdateDoctorScheduleRequest;
import com.hospital.smart_hospital.schedule.dto.response.DoctorScheduleResponse;
import com.hospital.smart_hospital.schedule.enitiy.DoctorSchedule;
import com.hospital.smart_hospital.schedule.mapper.DoctorScheduleMapper;
import com.hospital.smart_hospital.schedule.repository.DoctorScheduleRepository;
import com.hospital.smart_hospital.schedule.service.DoctorScheduleService;

import com.hospital.smart_hospital.security.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorScheduleServiceImpl implements DoctorScheduleService {

    private final DoctorScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorScheduleMapper mapper;
    private final AppointmentSlotService appointmentSlotService;
    private final AppointmentSlotRepository slotRepository;

    private final CurrentUserService
            currentUserService;

    @Override
    public DoctorScheduleResponse createSchedule(CreateDoctorScheduleRequest request) {

        Doctor doctor;

        // Admin bhejega toh doctorId hoga, Doctor bhejega toh JWT se nikalenge
        if (request.doctorId() != null) {
            doctor = getDoctor(request.doctorId());
        } else {
            doctor = currentUserService.getCurrentDoctor();
        }

        validateTime(request.startTime(), request.endTime());

        validateSlotDuration(
                request.startTime(),
                request.endTime(),
                request.slotDurationMinutes()
        );

        validateOverlap(
                doctor.getId(),
                request,
                -1L
        );

        DoctorSchedule schedule = mapper.toEntity(request);
        schedule.setDoctor(doctor);

        schedule = scheduleRepository.save(schedule);

        appointmentSlotService.generateSlots(schedule);

        return mapper.toResponse(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorScheduleResponse getScheduleById(Long id) {

        return mapper.toResponse(getSchedule(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DoctorScheduleResponse> getSchedules(String keyword,
                                                     Pageable pageable) {

        Page<DoctorSchedule> schedules;

        if (keyword == null || keyword.isBlank()) {

            schedules = scheduleRepository.findByActiveTrue(pageable);

        } else {

            schedules = scheduleRepository
                    .findByDoctorStaffUserFullNameContainingIgnoreCaseAndActiveTrue(
                            keyword,
                            pageable
                    );
        }

        return schedules.map(mapper::toResponse);
    }

    @Override
    public DoctorScheduleResponse updateSchedule(Long id,
                                                 UpdateDoctorScheduleRequest request) {

        DoctorSchedule schedule = getSchedule(id);

        validateTime(request.startTime(), request.endTime());

        validateSlotDuration(
                request.startTime(),
                request.endTime(),
                request.slotDurationMinutes()
        );

        validateOverlap(
                schedule.getDoctor().getId(),
                request,
                id
        );

        //  1. Purane non-booked slots delete karo (booked wale safe rahenge)
        slotRepository.deleteNonBookedSlotsByScheduleId(schedule.getId());

        // 2. Schedule ko update karo
        mapper.updateSchedule(request, schedule);
        schedule = scheduleRepository.save(schedule);

        // 3. Naye slots generate karo
        appointmentSlotService.generateSlots(schedule);

        return mapper.toResponse(schedule);
    }

    @Override
    public void deleteSchedule(Long id) {

        DoctorSchedule schedule = getSchedule(id);

        // Booked slots ko chhod kar baaki delete karo
        slotRepository.deleteNonBookedSlotsByScheduleId(schedule.getId());

        // Agar schedule ke saare slots booked hain, toh schedule ko soft delete (inactive) kar do
        schedule.setActive(false);
        scheduleRepository.save(schedule);
    }

    // ======================================================
    // Helper Methods
    // ======================================================

    private DoctorSchedule getSchedule(Long id) {

        return scheduleRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor schedule not found."
                        ));
    }

    private Doctor getDoctor(Long id) {

        return doctorRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found."
                        ));
    }

    private void validateTime(java.time.LocalTime start,
                              java.time.LocalTime end) {

        if (!start.isBefore(end)) {
            throw new BadRequestException(
                    "Start time must be before end time."
            );
        }
    }

    private void validateSlotDuration(java.time.LocalTime start,
                                      java.time.LocalTime end,
                                      Integer duration) {

        long totalMinutes =
                Duration.between(start, end).toMinutes();

        if (duration <= 0) {
            throw new BadRequestException(
                    "Invalid slot duration."
            );
        }

        if (duration > totalMinutes) {
            throw new BadRequestException(
                    "Slot duration exceeds schedule duration."
            );
        }
    }

    private void validateOverlap(Long doctorId,
                                 CreateDoctorScheduleRequest request,
                                 Long scheduleId) {

        boolean overlap;

        if (scheduleId == -1L) {

            overlap = scheduleRepository.existsOverlappingSchedule(
                    doctorId,
                    request.dayOfWeek(),
                    request.startTime(),
                    request.endTime()
            );

        } else {

            overlap = scheduleRepository.existsOverlappingSchedule(
                    doctorId,
                    request.dayOfWeek(),
                    request.startTime(),
                    request.endTime(),
                    scheduleId
            );
        }

        if (overlap) {
            throw new BadRequestException(
                    "Schedule overlaps with an existing schedule."
            );
        }
    }

    private void validateOverlap(Long doctorId,
                                 UpdateDoctorScheduleRequest request,
                                 Long scheduleId) {

        boolean overlap = scheduleRepository.existsOverlappingSchedule(
                doctorId,
                request.dayOfWeek(),
                request.startTime(),
                request.endTime(),
                scheduleId
        );

        if (overlap) {
            throw new BadRequestException(
                    "Schedule overlaps with an existing schedule."
            );
        }
    }
}

