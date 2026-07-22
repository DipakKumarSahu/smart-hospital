package com.hospital.smart_hospital.schedule.service;

import com.hospital.smart_hospital.schedule.dto.request.CreateDoctorScheduleRequest;
import com.hospital.smart_hospital.schedule.dto.request.UpdateDoctorScheduleRequest;
import com.hospital.smart_hospital.schedule.dto.response.DoctorScheduleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DoctorScheduleService {

    /**
     * Create a doctor's weekly schedule.
     */
    DoctorScheduleResponse createSchedule(CreateDoctorScheduleRequest request);

    /**
     * Get schedule by id.
     */
    DoctorScheduleResponse getScheduleById(Long id);

    /**
     * Get all schedules with optional search.
     */
    Page<DoctorScheduleResponse> getSchedules(String keyword,
                                              Pageable pageable);

    /**
     * Update an existing schedule.
     */
    DoctorScheduleResponse updateSchedule(Long id,
                                          UpdateDoctorScheduleRequest request);

    /**
     * Soft delete a schedule.
     */
    void deleteSchedule(Long id);
}