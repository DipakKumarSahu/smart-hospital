package com.hospital.smart_hospital.schedule.service;


import com.hospital.smart_hospital.schedule.dto.response.GenerateSlotsResponse;

import java.time.LocalDate;


public interface DoctorScheduleService {



        int generateSlots(Long doctorId, LocalDate date);

    }



//public interface DoctorScheduleService {
//
//    void generateSlots(GenerateSlotsRequest request);
//
//}