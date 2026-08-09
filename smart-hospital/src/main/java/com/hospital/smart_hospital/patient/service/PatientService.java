package com.hospital.smart_hospital.patient.service;


import com.hospital.smart_hospital.patient.dto.request.CreatePatientRequest;
import com.hospital.smart_hospital.patient.dto.request.UpdateMyProfileRequest;
import com.hospital.smart_hospital.patient.dto.request.UpdatePatientRequest;
import com.hospital.smart_hospital.patient.dto.response.PatientResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientService {

//    PatientResponse createPatient(CreatePatientRequest request);
//
//    PatientResponse getPatientById(Long id);
//
//    Page<PatientResponse> getPatients(String keyword,
//                                      Pageable pageable);
//
//    PatientResponse updatePatient(Long id,
//                                  UpdatePatientRequest request);
//
//    void deletePatient(Long id);


    // ====================================
    // Admin APIs
    // ====================================

    PatientResponse getPatientById(Long id);

    Page<PatientResponse> getPatients(
            String keyword,
            Pageable pageable
    );

    PatientResponse updatePatient(
            Long id,
            UpdatePatientRequest request
    );

    void deletePatient(Long id);

    // ====================================
    // Patient Self APIs
    // ====================================

    PatientResponse getMyProfile();

    PatientResponse updateMyProfile(
            UpdateMyProfileRequest request
    );



















}