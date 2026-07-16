package com.hospital.smart_hospital.department.service;


import com.hospital.smart_hospital.department.dto.request.CreateDepartmentRequest;
import com.hospital.smart_hospital.department.dto.request.UpdateDepartmentRequest;
import com.hospital.smart_hospital.department.dto.response.DepartmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartmentService {

    DepartmentResponse createDepartment(CreateDepartmentRequest request);

    DepartmentResponse getDepartmentById(Long id);

    List<DepartmentResponse> getAllDepartments();

    DepartmentResponse updateDepartment(
            Long id,
            UpdateDepartmentRequest request
    );

    void deleteDepartment(Long id);

    Page<DepartmentResponse> getAllActiveDepartments(Pageable pageable);

    Page<DepartmentResponse> searchDepartments(
            String keyword,
            Pageable pageable
    );

}