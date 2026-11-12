package com.hospital.smart_hospital.staff.controller;

import com.hospital.smart_hospital.common.dto.ApiResponse;
import com.hospital.smart_hospital.staff.dto.request.CreateStaffRequest;
import com.hospital.smart_hospital.staff.dto.response.StaffResponse;
import com.hospital.smart_hospital.staff.service.StaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> createStaff(@Valid @RequestBody CreateStaffRequest request) {
        staffService.createStaff(request);
        return ApiResponse.success("Staff created and welcome email sent.", null);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Page<StaffResponse>> getStaff(
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        return ApiResponse.success("Staff fetched successfully", staffService.getStaff(keyword, pageable));
    }

    @PatchMapping("/{id}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> toggleStatus(@PathVariable Long id) {
        staffService.toggleStaffStatus(id);
        return ApiResponse.success("Staff status updated", null);
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Map<String, Long>> getStats() {
        return ApiResponse.success("Stats fetched", staffService.getStaffStats());
    }
}