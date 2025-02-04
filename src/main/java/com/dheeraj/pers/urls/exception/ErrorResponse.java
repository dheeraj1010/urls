package com.dheeraj.pers.urls.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class ErrorResponse {
    private final int code;
    private final String message;
    private final String requestURI;

    private List<String> fieldErrors = new ArrayList<>();


    public ErrorResponse(int statusCode, String errorMessage, String requestURI) {
        super();
        this.code = statusCode;
        this.message = errorMessage;
        this.requestURI = requestURI;
    }

    private static String transformErrorMessage(FieldError fieldError) {
        return fieldError.getField() + " : " + fieldError.getDefaultMessage();
    }

    public void addFieldError(String message) {
        this.fieldErrors.add(message);
    }

    public void addAllFieldErrors(List<FieldError> errors) {
        if (CollectionUtils.isEmpty(errors)) {
            return;
        }
        List<String> transformedErrors = errors.stream().map(ErrorResponse::transformErrorMessage)
                .collect(Collectors.toList());
        this.fieldErrors.addAll(transformedErrors);
    }

    public List<String> getFieldErrors() {
        return Collections.unmodifiableList(this.fieldErrors);
    }
}
