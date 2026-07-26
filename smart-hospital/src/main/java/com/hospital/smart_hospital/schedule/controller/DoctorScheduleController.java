package com.hospital.smart_hospital.schedule.controller;



import com.hospital.smart_hospital.common.dto.ApiResponse;
import com.hospital.smart_hospital.schedule.dto.request.CreateDoctorScheduleRequest;
import com.hospital.smart_hospital.schedule.dto.request.UpdateDoctorScheduleRequest;
import com.hospital.smart_hospital.schedule.dto.response.DoctorScheduleResponse;
import com.hospital.smart_hospital.schedule.service.DoctorScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/doctor-schedules")
@RequiredArgsConstructor
public class DoctorScheduleController {

    private final DoctorScheduleService doctorScheduleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<DoctorScheduleResponse> createSchedule(
            @Valid @RequestBody CreateDoctorScheduleRequest request) {

        return ApiResponse.success(
                "Doctor schedule created successfully.",
                doctorScheduleService.createSchedule(request)
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<DoctorScheduleResponse> getScheduleById(
            @PathVariable Long id) {

        return ApiResponse.success(
                doctorScheduleService.getScheduleById(id)
        );
    }

    @GetMapping
    public ApiResponse<Page<DoctorScheduleResponse>> getSchedules(
            @RequestParam(required = false) String keyword,
            Pageable pageable) {

        return ApiResponse.success(
                doctorScheduleService.getSchedules(keyword, pageable)
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<DoctorScheduleResponse> updateSchedule(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDoctorScheduleRequest request) {

        return ApiResponse.success(
                "Doctor schedule updated successfully.",
                doctorScheduleService.updateSchedule(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSchedule(
            @PathVariable Long id) {

        doctorScheduleService.deleteSchedule(id);

        return ApiResponse.success(
                "Doctor schedule deleted successfully.",
                null
        );
    }
}