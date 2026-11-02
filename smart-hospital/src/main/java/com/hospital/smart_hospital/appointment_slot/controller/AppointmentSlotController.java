package com.hospital.smart_hospital.appointment_slot.controller;



import com.hospital.smart_hospital.appointment_slot.dto.request.BlockAppointmentSlotsRequest;
import com.hospital.smart_hospital.appointment_slot.dto.request.UpdateAppointmentSlotRequest;
import com.hospital.smart_hospital.appointment_slot.dto.response.AppointmentSlotResponse;
import com.hospital.smart_hospital.appointment_slot.service.AppointmentSlotService;
import com.hospital.smart_hospital.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/appointment-slots")
@RequiredArgsConstructor
public class AppointmentSlotController {

    private final AppointmentSlotService appointmentSlotService;

    @GetMapping("/{id}")
    public ApiResponse<AppointmentSlotResponse> getSlotById(
            @PathVariable Long id) {

        return ApiResponse.success(
                appointmentSlotService.getSlotById(id)
        );
    }

    @GetMapping
    public ApiResponse<Page<AppointmentSlotResponse>> getSlots(
            @RequestParam Long doctorId,
            @RequestParam LocalDate slotDate,
            Pageable pageable) {

        return ApiResponse.success(
                appointmentSlotService.getSlots(
                        doctorId,
                        slotDate,
                        pageable
                )
        );
    }

    @GetMapping("/available")
    public ApiResponse<List<AppointmentSlotResponse>> getAvailableSlots(
            @RequestParam Long doctorId,
            @RequestParam LocalDate slotDate) {

        return ApiResponse.success(
                appointmentSlotService.getAvailableSlots(
                        doctorId,
                        slotDate
                )
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<AppointmentSlotResponse> updateSlot(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAppointmentSlotRequest request) {

        return ApiResponse.success(
                "Appointment slot updated successfully.",
                appointmentSlotService.updateSlot(id, request)
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSlot(
            @PathVariable Long id) {

        appointmentSlotService.deleteSlot(id);

        return ApiResponse.success(
                "Appointment slot deleted successfully.",
                null
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @PatchMapping("/block")
    public ApiResponse<Void> blockSlots(
            @Valid @RequestBody BlockAppointmentSlotsRequest request) {

        appointmentSlotService.blockSlots(request);

        return ApiResponse.success(
                "Slots blocked successfully.",
                null
        );
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @PatchMapping("/unblock")
    public ApiResponse<Void> unblockSlots(
            @Valid @RequestBody BlockAppointmentSlotsRequest request) {

        appointmentSlotService.unblockSlots(request);

        return ApiResponse.success(
                "Slots unblocked successfully.",
                null
        );
    }

}