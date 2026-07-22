package com.hospital.smart_hospital.schedule.mapper;


import com.hospital.smart_hospital.schedule.dto.request.CreateDoctorScheduleRequest;
import com.hospital.smart_hospital.schedule.dto.request.UpdateDoctorScheduleRequest;
import com.hospital.smart_hospital.schedule.dto.response.DoctorScheduleResponse;

import com.hospital.smart_hospital.schedule.enitiy.DoctorSchedule;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DoctorScheduleMapper {

    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "active", constant = "true")
    DoctorSchedule toEntity(CreateDoctorScheduleRequest request);

    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "doctor.doctorCode", target = "doctorCode")
    @Mapping(source = "doctor.staff.user.fullName", target = "doctorName")
    DoctorScheduleResponse toResponse(DoctorSchedule schedule);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateSchedule(UpdateDoctorScheduleRequest request,
                        @MappingTarget DoctorSchedule schedule);
}
