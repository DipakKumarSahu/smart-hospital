package com.hospital.smart_hospital.schedule.dto.request;



import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenerateSlotsRequest {

    private Long doctorId;

    private LocalDate date;
}
