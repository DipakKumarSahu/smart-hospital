package com.hospital.smart_hospital.security.service;



import com.hospital.smart_hospital.doctor.entity.Doctor;
import com.hospital.smart_hospital.patient.entity.Patient;
import com.hospital.smart_hospital.staff.entity.Staff;
import com.hospital.smart_hospital.user.User;

public interface CurrentUserService {


    User getCurrentUser();

    Long getCurrentUserId();

    String getCurrentUserEmail();

    Staff getCurrentStaff();

    Patient getCurrentPatient();

    Doctor getCurrentDoctor();
}