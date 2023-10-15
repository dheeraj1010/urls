package com.dheeraj.pers.urls.controller;


import com.dheeraj.pers.urls.model.UrlEncodeRequest;
import com.dheeraj.pers.urls.model.UrlEncodeResponse;
import com.dheeraj.pers.urls.service.UrlShortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class UrlController {

    @Autowired
    private UrlShortService urlShortService;


    @PostMapping("/encode")
    public ResponseEntity<UrlEncodeResponse> encodeUrl(@RequestBody UrlEncodeRequest urlEncodeRequest) {
        UrlEncodeResponse urlEncodeResponse = new UrlEncodeResponse();
        urlEncodeResponse.setShortedUrl(urlShortService.encodeUrl(urlEncodeRequest));
        return new ResponseEntity<>(urlEncodeResponse, HttpStatus.OK);
    }


    @GetMapping("/redirect/{target}")
    public void redirectToTargetURL(@PathVariable String target, HttpServletResponse response) throws IOException {
        response.sendRedirect(urlShortService.decodeUrl(target));
    }

}
