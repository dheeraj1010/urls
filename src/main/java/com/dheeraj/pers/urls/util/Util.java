package com.dheeraj.pers.urls.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class Util {

    public static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Gson GSON = new Gson();

    private Util() {
        // To prevent instantiation
    }

    public static <T> String toJson(T obj) {
        return GSON.toJson(obj);
    }
}
