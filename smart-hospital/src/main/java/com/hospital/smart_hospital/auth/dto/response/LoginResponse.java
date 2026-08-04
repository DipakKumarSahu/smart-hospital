package com.hospital.smart_hospital.auth.dto.response;



import lombok.Builder;

@Builder
public record LoginResponse(

        String accessToken,

        String tokenType
) {
}