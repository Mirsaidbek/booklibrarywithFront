package com.bookstorage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SwaggerAccessInterceptor swaggerAccessInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(swaggerAccessInterceptor)
                .addPathPatterns("/swagger-ui/**", "/api-docs/**", "/swagger-ui.html");
    }
}
