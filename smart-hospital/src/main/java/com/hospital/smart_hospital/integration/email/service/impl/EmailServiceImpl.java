package com.hospital.smart_hospital.integration.email.service.impl;



import com.hospital.smart_hospital.integration.email.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(
            String to,
            String subject,
            String htmlContent) {

        MimeMessage mimeMessage =
                mailSender.createMimeMessage();

        try {

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            mimeMessage,
                            true,
                            "UTF-8"
                    );

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(
                    htmlContent,
                    true
            );

            mailSender.send(mimeMessage);

            log.info(
                    "Email sent successfully to {}",
                    to
            );

        } catch (MessagingException e) {

            log.error(
                    "Failed to send email to {}",
                    to,
                    e
            );

            throw new IllegalStateException(
                    "Unable to send email."
            );
        }
    }
}