package com.hospital.smart_hospital.appointment_slot.service;




import com.hospital.smart_hospital.appointment_slot.dto.request.UpdateAppointmentSlotRequest;
import com.hospital.smart_hospital.appointment_slot.dto.response.AppointmentSlotResponse;
import com.hospital.smart_hospital.schedule.enitiy.DoctorSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentSlotService {

    void generateSlots(DoctorSchedule doctorSchedule);

    AppointmentSlotResponse getSlotById(Long id);

    Page<AppointmentSlotResponse> getSlots(Long doctorId,
                                           LocalDate slotDate,
                                           Pageable pageable);

    List<AppointmentSlotResponse> getAvailableSlots(Long doctorId,
                                                    LocalDate slotDate);

    AppointmentSlotResponse updateSlot(Long id,
                                       UpdateAppointmentSlotRequest request);

    void deleteSlot(Long id);
}