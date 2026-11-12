package com.hospital.smart_hospital.auth.service;



public interface EmailVerificationService {

    void createAndSendVerificationToken(Long userId);

    void verifyEmail(String token);

    //Admin
    void sendDoctorWelcomeEmail(String fullName, String email, String rawPassword);

}
