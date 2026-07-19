package com.hospital.smart_hospital.patient.mapper;


import com.hospital.smart_hospital.patient.dto.request.CreatePatientRequest;
import com.hospital.smart_hospital.patient.dto.request.UpdatePatientRequest;
import com.hospital.smart_hospital.patient.dto.response.PatientResponse;
import com.hospital.smart_hospital.patient.entity.Patient;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    @Mapping(target = "patientCode", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "active", constant = "true")
    Patient toEntity(CreatePatientRequest request);

    @Mapping(source = "user.fullName", target = "fullName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.phoneNumber", target = "phoneNumber")
    PatientResponse toResponse(Patient patient);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "patientCode", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updatePatient(UpdatePatientRequest request,
                       @MappingTarget Patient patient);
}