package com.hospital.smart_hospital.common.mail;


import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.Map;


public class SmsTest {


        public static final String API_KEY = "8yHBpeqUrugS72ikOG5Nf1bLEm6KXVasTWv0zn3txDwFCYoRhZuMTISZJrnm0fjqvwKPNLakbH2YGoVO";

    @Test
        public void sms() {
            String url = "https://www.fast2sms.com/dev/bulkV2?route=q&message=YOUR_MESSAGE&numbers=1";

            HttpHeaders headers = new HttpHeaders();
            headers.set("authorization", API_KEY);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = new HashMap<>();
            body.put("route", "q");
            body.put("message", "Test SMS from SmartHospital!");
            body.put("language", "english");
            body.put("flash", 0);
            body.put("numbers", "6370940754");   // apna registered number daalo

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());
        }
    }

