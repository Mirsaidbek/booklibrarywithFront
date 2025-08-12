package com.bookstorage.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class SwaggerAccessInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(SwaggerAccessInterceptor.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String userAgent = request.getHeader("User-Agent");
        String remoteAddr = getClientIpAddress(request);
        String timestamp = LocalDateTime.now().format(formatter);

        // Log Swagger UI access
        if (requestURI.contains("/swagger-ui") || requestURI.contains("/api-docs")) {
            logger.info("🔍 Swagger Access - URI: {} | IP: {} | User-Agent: {} | Time: {}", 
                requestURI, remoteAddr, userAgent, timestamp);
        }

        return true;
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0];
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}
