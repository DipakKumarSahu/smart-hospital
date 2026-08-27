package com.hospital.smart_hospital.common.mail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
public class MailSendTest {

    @Autowired
   private JavaMailSender javaMailSender;

    @Test
        public void sendEmail() {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo("ankitsahu77889900@gmail.com");
            simpleMailMessage.setSubject("Greet");
            simpleMailMessage.setText("Kaise ho aap");
            simpleMailMessage.setFrom("dipak04092004@gmail.com");

            javaMailSender.send(simpleMailMessage);

        }

   }


