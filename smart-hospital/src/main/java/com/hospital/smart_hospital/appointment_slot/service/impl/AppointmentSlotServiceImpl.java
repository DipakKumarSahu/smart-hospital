package com.hospital.smart_hospital.appointment_slot.service.impl;



import com.hospital.smart_hospital.appointment_slot.dto.request.BlockAppointmentSlotsRequest;
import com.hospital.smart_hospital.appointment_slot.dto.request.UpdateAppointmentSlotRequest;
import com.hospital.smart_hospital.appointment_slot.dto.response.AppointmentSlotResponse;
import com.hospital.smart_hospital.appointment_slot.entity.AppointmentSlot;
import com.hospital.smart_hospital.appointment_slot.mapper.AppointmentSlotMapper;
import com.hospital.smart_hospital.appointment_slot.repository.AppointmentSlotRepository;
import com.hospital.smart_hospital.appointment_slot.service.AppointmentSlotService;
import com.hospital.smart_hospital.common.enums.SlotStatus;
import com.hospital.smart_hospital.exception.BadRequestException;
import com.hospital.smart_hospital.exception.ResourceNotFoundException;


import com.hospital.smart_hospital.schedule.enitiy.DoctorSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentSlotServiceImpl implements AppointmentSlotService {

    private static final int DAYS_TO_GENERATE = 30;

    private final AppointmentSlotRepository slotRepository;
    private final AppointmentSlotMapper mapper;

    @Override
    public void generateSlots(DoctorSchedule schedule) {

        LocalDate today = LocalDate.now();

        for (int i = 0; i < DAYS_TO_GENERATE; i++) {

            LocalDate date = today.plusDays(i);

            if (date.getDayOfWeek() != schedule.getDayOfWeek()) {
                continue;
            }

            generateSlotsForDate(schedule, date);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentSlotResponse getSlotById(Long id) {

        return mapper.toResponse(getSlot(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentSlotResponse> getSlots(Long doctorId,
                                                  LocalDate slotDate,
                                                  Pageable pageable) {

        return slotRepository
                .findByDoctorIdAndSlotDateAndActiveTrue(
                        doctorId,
                        slotDate,
                        pageable
                )
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.List<AppointmentSlotResponse> getAvailableSlots(
            Long doctorId,
            LocalDate slotDate) {

        return slotRepository
                .findByDoctorIdAndSlotDateAndStatusAndActiveTrue(
                        doctorId,
                        slotDate,
                        SlotStatus.AVAILABLE
                )
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public AppointmentSlotResponse updateSlot(Long id,
                                              UpdateAppointmentSlotRequest request) {

        AppointmentSlot slot = getSlot(id);

        mapper.updateSlot(request, slot);

        return mapper.toResponse(slotRepository.save(slot));
    }

    @Override
    public void deleteSlot(Long id) {

        AppointmentSlot slot = getSlot(id);

        slot.setActive(false);

        slotRepository.save(slot);
    }

    // =====================================================
    // Helper Methods
    // =====================================================

    private AppointmentSlot getSlot(Long id) {

        return slotRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Appointment slot not found."));
    }

    private void generateSlotsForDate(DoctorSchedule schedule,
                                      LocalDate slotDate) {

        LocalTime start = schedule.getStartTime();

        while (start.isBefore(schedule.getEndTime())) {

            LocalTime end = start.plusMinutes(
                    schedule.getSlotDurationMinutes());

            if (end.isAfter(schedule.getEndTime())) {
                break;
            }

            boolean exists = slotRepository
                    .existsByDoctorIdAndSlotDateAndStartTimeAndEndTimeAndActiveTrue(
                            schedule.getDoctor().getId(),
                            slotDate,
                            start,
                            end
                    );

            if (!exists) {

                AppointmentSlot slot = new AppointmentSlot();

                slot.setDoctor(schedule.getDoctor());
                slot.setDoctorSchedule(schedule);
                slot.setSlotDate(slotDate);
                slot.setStartTime(start);
                slot.setEndTime(end);
                slot.setStatus(SlotStatus.AVAILABLE);
                slot.setActive(true);

                slotRepository.save(slot);
            }

            start = end;
        }
    }


    @Override
    public void blockSlots(BlockAppointmentSlotsRequest request) {

        List<AppointmentSlot> slots =
                slotRepository
                        .findByDoctorIdAndSlotDateAndStartTimeGreaterThanEqualAndEndTimeLessThanEqualAndActiveTrue(
                                request.doctorId(),
                                request.slotDate(),
                                request.startTime(),
                                request.endTime()
                        );

        if (slots.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No appointment slots found."
            );
        }

        slots.forEach(slot -> {

            if (slot.getStatus() == SlotStatus.BOOKED) {
                throw new BadRequestException(
                        "Booked slots cannot be blocked."
                );
            }

            slot.setStatus(SlotStatus.BLOCKED);
        });

        slotRepository.saveAll(slots);
    }


    @Override
    public void unblockSlots(BlockAppointmentSlotsRequest request) {

        List<AppointmentSlot> slots =
                slotRepository
                        .findByDoctorIdAndSlotDateAndStartTimeGreaterThanEqualAndEndTimeLessThanEqualAndActiveTrue(
                                request.doctorId(),
                                request.slotDate(),
                                request.startTime(),
                                request.endTime()
                        );

        if (slots.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No appointment slots found."
            );
        }

        slots.forEach(slot -> {

            if (slot.getStatus() == SlotStatus.BLOCKED) {
                slot.setStatus(SlotStatus.AVAILABLE);
            }

        });

        slotRepository.saveAll(slots);
    }
}