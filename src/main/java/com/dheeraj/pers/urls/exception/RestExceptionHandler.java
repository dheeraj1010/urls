package com.dheeraj.pers.urls.exception;

import com.dheeraj.pers.urls.util.MessageConstants;
import com.fasterxml.jackson.core.io.JsonEOFException;
import org.apache.commons.lang3.SerializationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

    public static final String ERROR_IN = "ERROR IN:: ";
    public static final String ERROR_MESSAGE = ":: ERROR MESSAGE: ";
    private static final Logger LOGGER = LogManager.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException ex,
                                                                         HttpServletRequest request) {
        logError(ex);
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return processFieldErrors(fieldErrors, request);
    }

    @ExceptionHandler(value = SerializationException.class)
    public ResponseEntity<ErrorResponse> serialisationExceptionHandler(SerializationException ex, HttpServletRequest request) {
        logError(ex);
        ErrorResponse err = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), MessageConstants.SERIALIZATION_EXCEPTION + ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> processFieldErrors(List<FieldError> fieldErrors, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), MessageConstants.VALIDATION_EXCEPTION,
                request.getRequestURI());
        error.addAllFieldErrors(fieldErrors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ErrorResponse> numberFormatExceptionHandler(Exception ex, HttpServletRequest request) {
        logError(ex);
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), MessageConstants.NUMBER_FORMAT_EXCEPTION,
                request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonEOFException.class)
    public ResponseEntity<ErrorResponse> jsonEOFExceptionExceptionHandler(Exception ex, HttpServletRequest request) {
        logError(ex);
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), MessageConstants.JSON_EOF_EXCEPTION,
                request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(com.fasterxml.jackson.databind.JsonMappingException.class)
    public ResponseEntity<ErrorResponse> jsonMappingExceptionExceptionHandler(Exception ex,
                                                                              HttpServletRequest request) {
        logError(ex);
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), MessageConstants.JSON_MAPPING_EXCEPTION,
                request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(com.fasterxml.jackson.databind.exc.MismatchedInputException.class)
    public ResponseEntity<ErrorResponse> mismatchedInputExceptionExceptionHandler(Exception ex,
                                                                                  HttpServletRequest request) {
        logError(ex);
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), MessageConstants.MISMATCH_INPUT_EXCEPTION,
                request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> messageNotReadableExceptionHandler(Exception ex, HttpServletRequest request) {
        logError(ex);
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), MessageConstants.NOT_READABLE_EXCEPTION,
                request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex, HttpServletRequest request) {
        logError(ex);
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), MessageConstants.ERROR_REST_EXCEPTION_HANDLER,
                request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    private static void logError(Exception ex) {
        LOGGER.error(ERROR_IN + "{}" + ERROR_MESSAGE + "{}", ex.getClass().getSimpleName(), ex.getMessage());
    }
}




