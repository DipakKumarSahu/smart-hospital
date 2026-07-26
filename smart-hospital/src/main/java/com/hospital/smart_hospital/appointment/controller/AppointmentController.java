package com.hospital.smart_hospital.appointment.controller;

import com.hospital.smart_hospital.appointment.dto.request.CreateAppointmentRequest;
import com.hospital.smart_hospital.appointment.dto.request.UpdateAppointmentRequest;
import com.hospital.smart_hospital.appointment.dto.response.AppointmentResponse;
import com.hospital.smart_hospital.appointment.service.AppointmentService;
import com.hospital.smart_hospital.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ApiResponse<AppointmentResponse> createAppointment(
            @Valid @RequestBody CreateAppointmentRequest request) {

        return ApiResponse.success(
                "Appointment created successfully.",
                appointmentService.createAppointment(request)
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<AppointmentResponse> getAppointmentById(
            @PathVariable Long id) {

        return ApiResponse.success(
                appointmentService.getAppointmentById(id)
        );
    }

    @GetMapping
    public ApiResponse<Page<AppointmentResponse>> getAppointments(
            @RequestParam(required = false) String keyword,
            Pageable pageable) {

        return ApiResponse.success(
                appointmentService.getAppointments(keyword, pageable)
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<AppointmentResponse> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAppointmentRequest request) {

        return ApiResponse.success(
                "Appointment updated successfully.",
                appointmentService.updateAppointment(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteAppointment(
            @PathVariable Long id) {

        appointmentService.deleteAppointment(id);

        return ApiResponse.success(
                "Appointment deleted successfully.",
                null
        );
    }
}



