package com.hospital.smart_hospital.common.dto;
////
////
//import lombok.Builder;
//import lombok.Getter;
//
//import java.time.LocalDateTime;
//
//@Getter
//@Builder
//public class ApiResponse<T> {
//
//    private boolean success;
//
//    private String message;
//
//    private T data;
//
//    @Builder.Default
//    private LocalDateTime timestamp = LocalDateTime.now();
//
//}




//package com.hospital.smart_hospital.common.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message("Success")
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> failure(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }
}