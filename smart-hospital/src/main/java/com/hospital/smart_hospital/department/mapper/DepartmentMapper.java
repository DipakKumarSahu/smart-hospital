//package com.hospital.smart_hospital.department.mapper;
//
//
//
//import com.hospital.smart_hospital.department.dto.request.CreateDepartmentRequest;
//import com.hospital.smart_hospital.department.dto.response.DepartmentResponse;
//import com.hospital.smart_hospital.department.entity.Department;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DepartmentMapper {
//
//    public Department toEntity(CreateDepartmentRequest request) {
//
//        return Department.builder()
//                .departmentName(request.getDepartmentName().trim())
//                .description(request.getDescription())
//                .build();
//    }
//
//    public DepartmentResponse toResponse(Department department) {
//
//        return DepartmentResponse.builder()
//                .id(department.getId())
//                .departmentName(department.getDepartmentName())
//                .description(department.getDescription())
//                .build();
//    }
//}
// These above code is Manual Mapper.



package com.hospital.smart_hospital.department.mapper;

import com.hospital.smart_hospital.department.dto.request.CreateDepartmentRequest;
import com.hospital.smart_hospital.department.dto.response.DepartmentResponse;
import com.hospital.smart_hospital.department.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "createdAt", ignore = true)
//    @Mapping(target = "updatedAt", ignore = true)
//    @Mapping(target = "active", ignore = true)
    Department toEntity(CreateDepartmentRequest request);

    DepartmentResponse toResponse(Department department);

}