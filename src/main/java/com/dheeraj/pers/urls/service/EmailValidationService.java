package com.dheeraj.pers.urls.service;

import com.dheeraj.pers.urls.model.EmailValidationRequest;
import com.dheeraj.pers.urls.model.EmailValidationResponse;

public interface EmailValidationService {

    EmailValidationResponse validateEmail(EmailValidationRequest emailValidationRequest);
}
