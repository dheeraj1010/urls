package com.dheeraj.pers.urls.model;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UrlEncodeRequest {
    private String url;
    private LocalDateTime expiry;
}
