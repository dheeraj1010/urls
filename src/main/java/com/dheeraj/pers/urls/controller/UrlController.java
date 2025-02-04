package com.dheeraj.pers.urls.controller;


import com.dheeraj.pers.urls.model.UrlEncodeRequest;
import com.dheeraj.pers.urls.model.UrlEncodeResponse;
import com.dheeraj.pers.urls.service.UrlShortService;
import com.dheeraj.pers.urls.util.HttpReqRespUtils;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@CrossOrigin
public class UrlController {

    private static final Logger logger = LogManager.getLogger(UrlController.class);


    @Autowired
    private UrlShortService urlShortService;


    @PostMapping("/encode")
    @RateLimiter(name = "apiRateLimiter")
    public ResponseEntity<UrlEncodeResponse> encodeUrl(@RequestBody UrlEncodeRequest urlEncodeRequest, HttpServletRequest request) {
        urlEncodeRequest.setClientIp(HttpReqRespUtils.getClientIpAddressIfServletRequestExist(request));
        logger.info("Received request to encode URL: {}", urlEncodeRequest.getUrl());
        UrlEncodeResponse urlEncodeResponse = new UrlEncodeResponse();
        urlEncodeResponse.setShortedUrl(urlShortService.encodeUrl(urlEncodeRequest));
        return new ResponseEntity<>(urlEncodeResponse, HttpStatus.OK);
    }


    @GetMapping("{target}")
    @RateLimiter(name = "apiRateLimiter")
    public void redirectToTargetURL(@PathVariable String target, HttpServletResponse response) throws IOException {
        logger.info("Received request to redirect to: {}", target);
        response.sendRedirect(urlShortService.decodeUrl(target));
    }

}
