package com.caa.backend.dto.ResponseDTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Generic API response wrapper. Used for success/error messages
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class APIResponseDTO<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    //Creates a successful response with data
    public static <T> APIResponseDTO<T> success(String message, T data) {
        return APIResponseDTO.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Creates a successful response without data
    public static <T> APIResponseDTO<T> success(String message) {
        return success(message, null);
    }

    // Error response
    public static <T> APIResponseDTO<T> error(String message) {
        return APIResponseDTO.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }


    // Error response with data
    public static <T> APIResponseDTO<T> error(String message, T data) {
        return APIResponseDTO.<T>builder()
                .success(false)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
