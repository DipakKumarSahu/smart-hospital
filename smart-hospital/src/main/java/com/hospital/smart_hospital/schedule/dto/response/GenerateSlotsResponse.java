package com.hospital.smart_hospital.schedule.dto.response;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenerateSlotsResponse {

    private Long doctorId;

    private int totalSlotsGenerated;

    private String message;

}
