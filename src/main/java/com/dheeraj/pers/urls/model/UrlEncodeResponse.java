package com.dheeraj.pers.urls.model;


import lombok.Data;

@Data
public class UrlEncodeResponse {
    private String shortedUrl;
    private String errorMsg;
}
