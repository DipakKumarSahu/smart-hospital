package com.hospital.smart_hospital.appointment.mapper;



import com.hospital.smart_hospital.appointment.dto.request.CreateAppointmentRequest;

import com.hospital.smart_hospital.appointment.dto.request.UpdateAppointmentRequest;
import com.hospital.smart_hospital.appointment.dto.response.AppointmentResponse;
import com.hospital.smart_hospital.appointment.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appointmentCode", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "appointmentSlot", ignore = true)
    @Mapping(target = "appointmentDate", ignore = true)
    @Mapping(target = "appointmentTime", ignore = true)
    @Mapping(target = "appointmentStatus", ignore = true)
//    @Mapping(target = "paymentStatus", ignore = true)
    @Mapping(target = "active", ignore = true)
    Appointment toEntity(CreateAppointmentRequest request);

    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "appointmentSlot", ignore = true)
    @Mapping(target = "appointmentCode", ignore = true)
    @Mapping(target = "appointmentDate", ignore = true)
    @Mapping(target = "appointmentTime", ignore = true)
    @Mapping(target = "visitType", ignore = true)
    @Mapping(target = "bookingSource", ignore = true)
    void updateAppointment(UpdateAppointmentRequest request,
                           @MappingTarget Appointment appointment);


    @Mapping(source = "appointmentStatus", target = "status")

    @Mapping(target = "active", ignore = true)

    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "patient.user.fullName", target = "patientName")

    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "doctor.doctorCode", target = "doctorCode")
    @Mapping(source = "doctor.staff.user.fullName", target = "doctorName")

    @Mapping(source = "appointmentSlot.id", target = "appointmentSlotId")

    AppointmentResponse toResponse(Appointment appointment);
}