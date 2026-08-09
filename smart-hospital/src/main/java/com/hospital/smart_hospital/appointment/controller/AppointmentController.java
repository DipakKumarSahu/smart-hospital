package com.hospital.smart_hospital.appointment.controller;

import com.hospital.smart_hospital.appointment.dto.request.CreateAppointmentRequest;
import com.hospital.smart_hospital.appointment.dto.request.PatientBookAppointmentRequest;
import com.hospital.smart_hospital.appointment.dto.request.UpdateAppointmentRequest;
import com.hospital.smart_hospital.appointment.dto.response.AppointmentResponse;
import com.hospital.smart_hospital.appointment.service.AppointmentService;
import com.hospital.smart_hospital.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    // =====================================================
    // Admin / Receptionist APIs
    // =====================================================

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @PostMapping("/appointments")
    public ApiResponse<AppointmentResponse> createAppointment(
            @Valid @RequestBody CreateAppointmentRequest request) {

        return ApiResponse.success(
                "Appointment created successfully.",
                appointmentService.createAppointment(request)
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @GetMapping("/appointments/{id}")
    public ApiResponse<AppointmentResponse> getAppointmentById(
            @PathVariable Long id) {

        return ApiResponse.success(
                appointmentService.getAppointmentById(id)
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @GetMapping("/appointments")
    public ApiResponse<Page<AppointmentResponse>> getAppointments(
            @RequestParam(required = false) String keyword,
            Pageable pageable) {

        return ApiResponse.success(
                appointmentService.getAppointments(
                        keyword,
                        pageable
                )
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @PutMapping("/appointments/{id}")
    public ApiResponse<AppointmentResponse> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAppointmentRequest request) {

        return ApiResponse.success(
                "Appointment updated successfully.",
                appointmentService.updateAppointment(
                        id,
                        request
                )
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @DeleteMapping("/appointments/{id}")
    public ApiResponse<Void> deleteAppointment(
            @PathVariable Long id) {

        appointmentService.deleteAppointment(id);

        return ApiResponse.success(
                "Appointment deleted successfully.",
                null
        );
    }

    // =====================================================
    // Patient Self-Service APIs
    // =====================================================

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping("/patient/appointments")
    public ApiResponse<AppointmentResponse> bookAppointment(
            @Valid @RequestBody PatientBookAppointmentRequest request) {

        return ApiResponse.success(
                "Appointment booked successfully.",
                appointmentService.bookAppointmentAsPatient(
                        request
                )
        );
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/patient/appointments")
    public ApiResponse<Page<AppointmentResponse>> getMyAppointments(
            Pageable pageable) {

        return ApiResponse.success(
                appointmentService.getMyAppointments(
                        pageable
                )
        );
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/patient/appointments/{id}")
    public ApiResponse<AppointmentResponse> getMyAppointmentById(
            @PathVariable Long id) {

        return ApiResponse.success(
                appointmentService.getMyAppointmentById(id)
        );
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PatchMapping("/patient/appointments/{id}/cancel")
    public ApiResponse<Void> cancelMyAppointment(
            @PathVariable Long id) {

        appointmentService.cancelMyAppointment(id);

        return ApiResponse.success(
                "Appointment cancelled successfully.",
                null
        );
    }
}


