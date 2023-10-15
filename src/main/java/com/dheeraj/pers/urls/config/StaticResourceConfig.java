package com.dheeraj.pers.urls.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resume.pdf")
                .addResourceLocations("file:/home/dheerajkumar/Downloads/Personal/Codes/URL1/urls/src/main/resources/static/");
    }
}
