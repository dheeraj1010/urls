package com.dheeraj.pers.urls.config;

import com.dheeraj.pers.urls.exception.RestTemplateExceptionHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Collections;


@Configuration
public class RestTemplateConfig {

    private static final int TIMEOUT = 300000;

    @Bean
    RestTemplate restTemplateWithConnectReadTimeout() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(TIMEOUT))
                .setReadTimeout(Duration.ofMillis(TIMEOUT))
                .errorHandler(new RestTemplateExceptionHandler())
                .build();
    }
}

