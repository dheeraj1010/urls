package com.dheeraj.pers.urls.model;


import lombok.Data;

@Data
public class EmailValidationRequest {
    private String email;
    private String clientIp;
}
