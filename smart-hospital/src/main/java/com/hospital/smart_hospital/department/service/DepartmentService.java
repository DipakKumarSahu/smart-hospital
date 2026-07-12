package com.hospital.smart_hospital.department.service;


import com.hospital.smart_hospital.department.dto.request.CreateDepartmentRequest;
import com.hospital.smart_hospital.department.dto.response.DepartmentResponse;

import java.util.List;

public interface DepartmentService {

    DepartmentResponse createDepartment(CreateDepartmentRequest request);

    DepartmentResponse getDepartmentById(Long id);

    List<DepartmentResponse> getAllDepartments();

}