package com.dheeraj.pers.urls.util;

public class MessageConstants {

    public static final String ERROR_REST_EXCEPTION_HANDLER = "The request could not be understood by the server due to malformed syntax.";
    public static final String NOT_READABLE_EXCEPTION = "invalid json";
    public static final String INVALID_DATE_EXCEPTION = "invalid DateTime";
    public static final String MISMATCH_INPUT_EXCEPTION = "invalid json near " + "{";
    public static final String JSON_MAPPING_EXCEPTION = "invalid json missing " + "\"";
    public static final String JSON_EOF_EXCEPTION = "invalid json near " + "}";
    public static final String NUMBER_FORMAT_EXCEPTION = "invalid number input";
    public static final String VALIDATION_EXCEPTION = "Validation Exception";
    public static final String BAD_SQL_QUERY = "Bad SQL Query";
    public static final String DATA_ACCESS_EXCEPTION = "Something went wrong!!";
    public static final String DATA_INTEGRITY_EXCEPTION = "Data Integrity Exception(Foreign Key Issue)";
    public static final String BAD_SQL_GRAMMAR_EXCEPTION = "Relation does not exist";
    public static final String DATA_BASE_CONNECTION_TIME_OUT_EXCEPTION = "Server is unable to handle your request";
    public static final String DATA_INTEGRITY_VIOLATION_EXCEPTION = "Invalid id = ";
    public static final int ZERO_MAPPED = 0;

    public static final String ERROR_MESSAGE = "Error: ";

    public static final String SERIALIZATION_EXCEPTION = "SERIALIZATION EXCEPTION : ";

    private MessageConstants() {
        // To prevent instantiation
    }
}
