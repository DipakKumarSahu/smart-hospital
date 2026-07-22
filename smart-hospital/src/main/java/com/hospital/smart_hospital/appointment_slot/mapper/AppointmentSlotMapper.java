package com.hospital.smart_hospital.appointment_slot.mapper;


import com.hospital.smart_hospital.appointment_slot.dto.request.UpdateAppointmentSlotRequest;
import com.hospital.smart_hospital.appointment_slot.dto.response.AppointmentSlotResponse;
import com.hospital.smart_hospital.appointment_slot.entity.AppointmentSlot;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AppointmentSlotMapper {

    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "doctor.doctorCode", target = "doctorCode")
    @Mapping(source = "doctor.staff.user.fullName", target = "doctorName")
    @Mapping(source = "doctorSchedule.id", target = "doctorScheduleId")
    AppointmentSlotResponse toResponse(AppointmentSlot appointmentSlot);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "doctorSchedule", ignore = true)
    @Mapping(target = "slotDate", ignore = true)
    @Mapping(target = "startTime", ignore = true)
    @Mapping(target = "endTime", ignore = true)
    void updateSlot(UpdateAppointmentSlotRequest request,
                    @MappingTarget AppointmentSlot appointmentSlot);
}