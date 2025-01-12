package com.dheeraj.pers.urls.model;


import lombok.Data;


@Data
public class UrlEncodeRequest {
    private String url;
    private String clientIp;
}
