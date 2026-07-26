package com.hospital.smart_hospital.doctor.service.impl;

import com.hospital.smart_hospital.department.entity.Department;
import com.hospital.smart_hospital.department.repository.DepartmentRepository;
import com.hospital.smart_hospital.doctor.dto.request.CreateDoctorRequest;
import com.hospital.smart_hospital.doctor.dto.request.UpdateDoctorRequest;
import com.hospital.smart_hospital.doctor.dto.response.DoctorResponse;

import com.hospital.smart_hospital.doctor.entity.Doctor;
import com.hospital.smart_hospital.doctor.mapper.DoctorMapper;
import com.hospital.smart_hospital.doctor.repository.DoctorRepository;
import com.hospital.smart_hospital.doctor.service.DoctorService;
import com.hospital.smart_hospital.exception.BadRequestException;
import com.hospital.smart_hospital.exception.DuplicateResourceException;
import com.hospital.smart_hospital.exception.ResourceNotFoundException;
import com.hospital.smart_hospital.role.RoleType;
import com.hospital.smart_hospital.staff.entity.Staff;
import com.hospital.smart_hospital.staff.repository.StaffRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;


@Service
@RequiredArgsConstructor
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final StaffRepository staffRepository;
    private final DepartmentRepository departmentRepository;




    @Override
    public DoctorResponse createDoctor(CreateDoctorRequest request) {

        validateDoctor(request);

        Staff staff = getStaff(request.getStaffId());

        validateDoctorStaff(staff);

        Department department = getDepartment(request.getDepartmentId());

        Doctor doctor = doctorMapper.toEntity(request);

        doctor.setDoctorCode(generateDoctorCode());

        doctor.setStaff(staff);

        doctor.setDepartment(department);

        Doctor savedDoctor = doctorRepository.save(doctor);

        return doctorMapper.toResponse(savedDoctor);
    }

    private void validateDoctor(CreateDoctorRequest request) {

        if (doctorRepository.existsByLicenseNumberIgnoreCase(
                request.getLicenseNumber())) {

            throw new DuplicateResourceException(
                    "License number already exists."
            );
        }

        if (doctorRepository.existsByStaffId(
                request.getStaffId())) {

            throw new DuplicateResourceException(
                    "This staff is already registered as a doctor."
            );
        }
    }

    private Staff getStaff(Long staffId) {

        return staffRepository.findByIdAndActiveTrue(staffId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Staff not found."
                        ));
    }

    private Department getDepartment(Long departmentId) {

        return departmentRepository.findById(departmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found."
                        ));
    }

    private void validateDoctorStaff(Staff staff) {

        boolean isDoctor = staff.getUser()
                .getRoles()
                .stream()
                .anyMatch(role ->
                        role.getName() == RoleType.ROLE_DOCTOR);

        if (!isDoctor) {

            throw new BadRequestException(
                    "Selected staff is not assigned the DOCTOR role."
            );
        }
    }

    private String generateDoctorCode() {

        long count = doctorRepository.count() + 1;

        return String.format(
                "DOC-%d-%06d",
                Year.now().getValue(),
                count
        );
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorResponse getDoctorById(Long id) {

        Doctor doctor = getDoctor(id);

        return doctorMapper.toResponse(doctor);
    }

    private Doctor getDoctor(Long id) {

        return doctorRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found."
                        ));
    }

//    @Override
//    @Transactional(readOnly = true)
//    public Page<DoctorResponse> getAllDoctors(Pageable pageable) {
//
//        return doctorRepository.findByActiveTrue(pageable)
//                .map(doctorMapper::toResponse);
//    }

    @Override
    public DoctorResponse updateDoctor(Long id,
                                       UpdateDoctorRequest request) {

        Doctor doctor = getDoctor(id);

        Department department =
                getDepartment(request.getDepartmentId());

        doctorMapper.updateDoctor(request, doctor);

        doctor.setDepartment(department);

        Doctor updatedDoctor =
                doctorRepository.save(doctor);

        return doctorMapper.toResponse(updatedDoctor);
    }

    @Override
    public void deleteDoctor(Long id) {

        Doctor doctor = getDoctor(id);

        if (!doctor.isActive()) {
            throw new BadRequestException(
                    "Doctor is already inactive."
            );
        }

        doctor.setActive(false);

        doctorRepository.save(doctor);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<DoctorResponse> getDoctors(
            String keyword,
            Pageable pageable) {

        if (keyword == null || keyword.isBlank()) {

            return doctorRepository.findByActiveTrue(pageable)
                    .map(doctorMapper::toResponse);
        }

        Page<Doctor> doctors =
                doctorRepository
                        .findBySpecializationContainingIgnoreCaseAndActiveTrue(
                                keyword,
                                pageable
                        );

        if (doctors.isEmpty()) {

            doctors =
                    doctorRepository
                            .findByStaffUserFullNameContainingIgnoreCaseAndActiveTrue(
                                    keyword,
                                    pageable
                            );
        }

        return doctors.map(doctorMapper::toResponse);
    }







}
