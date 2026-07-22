package com.hospital.smart_hospital.appointment.service;

import com.hospital.smart_hospital.appointment.dto.request.CreateAppointmentRequest;
import com.hospital.smart_hospital.appointment.dto.request.UpdateAppointmentRequest;
import com.hospital.smart_hospital.appointment.dto.response.AppointmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AppointmentService {

    AppointmentResponse createAppointment(CreateAppointmentRequest request);

    AppointmentResponse getAppointmentById(Long id);

    Page<AppointmentResponse> getAppointments(String keyword,
                                              Pageable pageable);

    AppointmentResponse updateAppointment(Long id,
                                          UpdateAppointmentRequest request);

    void deleteAppointment(Long id);
}