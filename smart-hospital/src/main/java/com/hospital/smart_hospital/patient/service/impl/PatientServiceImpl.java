package com.hospital.smart_hospital.patient.service.impl;

import com.hospital.smart_hospital.exception.DuplicateResourceException;
import com.hospital.smart_hospital.exception.ResourceNotFoundException;
import com.hospital.smart_hospital.patient.dto.request.CreatePatientRequest;
import com.hospital.smart_hospital.patient.dto.request.UpdatePatientRequest;
import com.hospital.smart_hospital.patient.dto.response.PatientResponse;
import com.hospital.smart_hospital.patient.entity.Patient;
import com.hospital.smart_hospital.patient.mapper.PatientMapper;
import com.hospital.smart_hospital.patient.repository.PatientRepository;
import com.hospital.smart_hospital.patient.service.PatientService;
import com.hospital.smart_hospital.user.User;

import com.hospital.smart_hospital.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final PatientMapper patientMapper;

    @Override
    public PatientResponse createPatient(CreatePatientRequest request) {

        validatePatient(request);

        User user = getUser(request.getUserId());

        Patient patient = patientMapper.toEntity(request);

        patient.setUser(user);
        patient.setPatientCode(generatePatientCode());

        Patient savedPatient = patientRepository.save(patient);

        return patientMapper.toResponse(savedPatient);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponse getPatientById(Long id) {

        return patientMapper.toResponse(getPatient(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PatientResponse> getPatients(String keyword,
                                             Pageable pageable) {

        Page<Patient> patients;

        if (keyword == null || keyword.isBlank()) {

            patients = patientRepository.findByActiveTrue(pageable);

        } else {

            patients =
                    patientRepository
                            .findByUserFullNameContainingIgnoreCaseAndActiveTrue(
                                    keyword,
                                    pageable
                            );
        }

        return patients.map(patientMapper::toResponse);
    }

    @Override
    public PatientResponse updatePatient(Long id,
                                         UpdatePatientRequest request) {

        Patient patient = getPatient(id);

        patientMapper.updatePatient(request, patient);

        Patient updatedPatient = patientRepository.save(patient);

        return patientMapper.toResponse(updatedPatient);
    }

    @Override
    public void deletePatient(Long id) {

        Patient patient = getPatient(id);

        patient.setActive(false);

        patientRepository.save(patient);
    }

    // ----------------- Helper Methods -----------------

    private Patient getPatient(Long id) {

        return patientRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with id: " + id
                        ));
    }

    private User getUser(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        ));
    }

    private void validatePatient(CreatePatientRequest request) {

        if (patientRepository.existsByUserId(request.getUserId())) {

            throw new DuplicateResourceException(
                    "Patient already exists for this user."
            );
        }
    }

    private String generatePatientCode() {

        long count = patientRepository.count() + 1;

        return String.format(
                "PAT-%d-%06d",
                Year.now().getValue(),
                count
        );
    }
}