package com.hospital.smart_hospital.doctor.mapper;


import com.hospital.smart_hospital.doctor.dto.request.CreateDoctorRequest;
import com.hospital.smart_hospital.doctor.dto.request.UpdateDoctorRequest;
import com.hospital.smart_hospital.doctor.dto.response.DoctorResponse;
import com.hospital.smart_hospital.doctor.entity.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

//    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctorCode", ignore = true)
    @Mapping(target = "staff", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "active", ignore = true)
    Doctor toEntity(CreateDoctorRequest request);

    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.departmentName", target = "departmentName")
    @Mapping(source = "staff.id", target = "staffId")
    @Mapping(source = "staff.user.fullName", target = "staffName")
    DoctorResponse toResponse(Doctor doctor);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctorCode", ignore = true)
    @Mapping(target = "licenseNumber", ignore = true)
    @Mapping(target = "staff", ignore = true)
    @Mapping(target = "department", ignore = true)
    void updateDoctor(UpdateDoctorRequest request,
                      @MappingTarget Doctor doctor);
}