package com.dheeraj.pers.urls.exception;

import org.springframework.http.ResponseEntity;

public class ResponseEntityErrorException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    private final ResponseEntity<ErrorResponse> serviceErrorResponse;

    public ResponseEntityErrorException(ResponseEntity<ErrorResponse> serviceErrorResponseResponse) {
        this.serviceErrorResponse = serviceErrorResponseResponse;
    }

    public ResponseEntity<ErrorResponse> getServiceErrorResponseResponse() {
        return serviceErrorResponse;
    }
}
