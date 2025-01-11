package com.dheeraj.pers.urls.controller;


import com.dheeraj.pers.urls.model.UrlEncodeRequest;
import com.dheeraj.pers.urls.model.UrlEncodeResponse;
import com.dheeraj.pers.urls.service.UrlShortService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class UrlController {

    private static final Logger logger = LogManager.getLogger(UrlController.class);


    @Autowired
    private UrlShortService urlShortService;


    @PostMapping("/encode")
    public ResponseEntity<UrlEncodeResponse> encodeUrl(@RequestBody UrlEncodeRequest urlEncodeRequest) {
        logger.info("Received request to encode URL: " + urlEncodeRequest.getUrl());
        UrlEncodeResponse urlEncodeResponse = new UrlEncodeResponse();
        urlEncodeResponse.setShortedUrl(urlShortService.encodeUrl(urlEncodeRequest));
        return new ResponseEntity<>(urlEncodeResponse, HttpStatus.OK);
    }


    @GetMapping("/redirect/{target}")
    public void redirectToTargetURL(@PathVariable String target, HttpServletResponse response) throws IOException {
        logger.info("Received request to redirect to: " + target);
        response.sendRedirect(urlShortService.decodeUrl(target));
    }

}
