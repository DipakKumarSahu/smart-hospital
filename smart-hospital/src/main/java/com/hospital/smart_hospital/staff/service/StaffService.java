package com.hospital.smart_hospital.staff.service;

import com.hospital.smart_hospital.staff.dto.request.CreateStaffRequest;
import com.hospital.smart_hospital.staff.dto.response.StaffResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StaffService {

    void createStaff(CreateStaffRequest request);
    Page<StaffResponse> getStaff(String keyword, Pageable pageable);
    void toggleStaffStatus(Long id);
    java.util.Map<String, Long> getStaffStats(); // Dashboard ke liye
}