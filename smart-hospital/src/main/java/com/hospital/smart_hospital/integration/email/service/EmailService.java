package com.hospital.smart_hospital.integration.email.service;


public interface EmailService {

    void sendEmail(
            String to,
            String subject,
            String htmlContent
    );
}