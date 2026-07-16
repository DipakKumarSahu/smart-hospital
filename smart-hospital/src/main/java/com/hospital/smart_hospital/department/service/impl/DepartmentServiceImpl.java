package com.hospital.smart_hospital.department.service.impl;


import com.hospital.smart_hospital.department.dto.request.CreateDepartmentRequest;
import com.hospital.smart_hospital.department.dto.request.UpdateDepartmentRequest;
import com.hospital.smart_hospital.department.dto.response.DepartmentResponse;
import com.hospital.smart_hospital.department.entity.Department;
import com.hospital.smart_hospital.department.mapper.DepartmentMapper;
import com.hospital.smart_hospital.department.repository.DepartmentRepository;
import com.hospital.smart_hospital.department.service.DepartmentService;
import com.hospital.smart_hospital.exception.BadRequestException;
import com.hospital.smart_hospital.exception.DuplicateResourceException;
import com.hospital.smart_hospital.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public DepartmentResponse createDepartment(CreateDepartmentRequest request) {

        validateDepartment(request);

        Department department = departmentMapper.toEntity(request);

        Department savedDepartment = departmentRepository.save(department);

        return departmentMapper.toResponse(savedDepartment);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(Long id) {

        Department department = getDepartment(id);

        return departmentMapper.toResponse(department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartments() {

        return departmentRepository.findAll()
                .stream()
                .map(departmentMapper::toResponse)
                .toList();
    }

    private void validateDepartment(CreateDepartmentRequest request) {

        if (departmentRepository.existsByDepartmentCodeIgnoreCase(
                request.getDepartmentCode())) {

            throw new DuplicateResourceException(
                    "Department code already exists.");
        }

        if (departmentRepository.existsByDepartmentNameIgnoreCase(
                request.getDepartmentName())) {

            throw new DuplicateResourceException(
                    "Department name already exists.");
        }
    }


    @Override
    public DepartmentResponse updateDepartment(
            Long id,
            UpdateDepartmentRequest request) {

        Department department = getDepartment(id);

        department.setDepartmentName(request.getDepartmentName());
        department.setDescription(request.getDescription());
        department.setActive(request.isActive());

        Department updatedDepartment =
                departmentRepository.save(department);

        return departmentMapper.toResponse(updatedDepartment);
    }


    @Override
    public void deleteDepartment(Long id) {

        Department department = getDepartment(id);

        if (!department.isActive()) {
            throw new BadRequestException("Department is already inactive.");
        }

        department.setActive(false);

        departmentRepository.save(department);
    }

//    @Override
//    @Transactional(readOnly = true)
//    public List<DepartmentResponse> getAllActiveDepartments() {
//
//        return departmentRepository.findByActiveTrue()
//                .stream()
//                .map(departmentMapper::toResponse)
//                .toList();
//    }

    private Department getDepartment(Long id) {

        return departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found."));
    }


    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentResponse> getAllActiveDepartments(Pageable pageable) {

        return departmentRepository
                .findByActiveTrue(pageable)
                .map(departmentMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentResponse> searchDepartments(
            String keyword,
            Pageable pageable) {

        return departmentRepository
                .findByDepartmentNameContainingIgnoreCaseAndActiveTrue(
                        keyword,
                        pageable
                )
                .map(departmentMapper::toResponse);
    }
}