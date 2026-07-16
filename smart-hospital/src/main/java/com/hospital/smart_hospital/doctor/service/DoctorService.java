package com.hospital.smart_hospital.doctor.service;


import com.hospital.smart_hospital.doctor.dto.request.CreateDoctorRequest;
import com.hospital.smart_hospital.doctor.dto.request.UpdateDoctorRequest;
import com.hospital.smart_hospital.doctor.dto.response.DoctorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DoctorService {

    DoctorResponse createDoctor(CreateDoctorRequest request);

    DoctorResponse getDoctorById(Long id);

//    Page<DoctorResponse> getAllDoctors(Pageable pageable);

    DoctorResponse updateDoctor(Long id,
                                UpdateDoctorRequest request);

    void deleteDoctor(Long id);

//    Page<DoctorResponse> searchDoctors(
//            String keyword,
//            Pageable pageable
//    );

    Page<DoctorResponse> getDoctors(
            String keyword,
            Pageable pageable
    );
}
