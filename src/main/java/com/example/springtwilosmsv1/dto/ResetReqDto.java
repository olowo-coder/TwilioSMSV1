package com.example.springtwilosmsv1.dto;

import lombok.Data;

@Data
public class ResetReqDto {
    private String phoneNumber;
    private String username;
    private String otp;
}
