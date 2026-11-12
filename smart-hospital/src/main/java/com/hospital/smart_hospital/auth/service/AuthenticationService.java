package com.hospital.smart_hospital.auth.service;


import com.hospital.smart_hospital.auth.dto.request.CreateDoctorByAdminRequest;
import com.hospital.smart_hospital.auth.dto.request.LoginRequest;
import com.hospital.smart_hospital.auth.dto.request.RegisterPatientRequest;
import com.hospital.smart_hospital.auth.dto.response.LoginResponse;

public interface AuthenticationService {

    LoginResponse login(LoginRequest request);

    void registerPatient(RegisterPatientRequest request);

    //
    public void createDoctorByAdmin(CreateDoctorByAdminRequest request);

}
