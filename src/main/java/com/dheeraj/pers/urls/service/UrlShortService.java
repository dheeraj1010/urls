package com.dheeraj.pers.urls.service;

import com.dheeraj.pers.urls.model.UrlEncodeRequest;

public interface UrlShortService {

    String encodeUrl(UrlEncodeRequest urlEncodeRequest);
    String decodeUrl(String target);
}
