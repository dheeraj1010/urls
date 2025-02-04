package com.dheeraj.pers.urls.exception;

import com.dheeraj.pers.urls.util.Util;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class RestTemplateExceptionHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        String result = new BufferedReader(new InputStreamReader(response.getBody())).lines().collect(Collectors.joining("\n"));
        ErrorResponse readValue = Util.MAPPER.readValue(result, ErrorResponse.class);
        throw new ResponseEntityErrorException(
                ResponseEntity.status(response.getRawStatusCode()).headers(response.getHeaders()).body(readValue));

    }

}