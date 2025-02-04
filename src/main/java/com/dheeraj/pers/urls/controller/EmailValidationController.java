package com.dheeraj.pers.urls.controller;

import com.dheeraj.pers.urls.model.EmailValidationRequest;
import com.dheeraj.pers.urls.model.EmailValidationResponse;
import com.dheeraj.pers.urls.service.EmailValidationService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class EmailValidationController {

    private static final Logger logger = LogManager.getLogger(EmailValidationController.class);


    @Autowired
    private EmailValidationService emailValidationService;

    @PostMapping("/validate-email")
    @RateLimiter(name = "apiRateLimiter")
    public ResponseEntity<EmailValidationResponse> validateEmail(@RequestBody EmailValidationRequest emailValidationRequest) {
        logger.info("RequestBody: {}", emailValidationRequest);
        EmailValidationResponse response = emailValidationService.validateEmail(emailValidationRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
