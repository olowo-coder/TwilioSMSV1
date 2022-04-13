package com.example.springtwilosmsv1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetResponseDto {
    private String message;
    private OtpStatus otpStatus;
}
