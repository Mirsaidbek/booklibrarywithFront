package com.bookstorage.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(LogUtil.class);
    
    /**
     * Log error with detailed information
     */
    public static void logError(String message, Throwable throwable) {
        logger.error("❌ ERROR: {} - {}", message, throwable.getMessage(), throwable);
    }
    
    /**
     * Log error with context information
     */
    public static void logError(String context, String message, Throwable throwable) {
        logger.error("❌ ERROR [{}]: {} - {}", context, message, throwable.getMessage(), throwable);
    }
    
    /**
     * Log error without exception
     */
    public static void logError(String message) {
        logger.error("❌ ERROR: {}", message);
    }
    
    /**
     * Log error with context
     */
    public static void logError(String context, String message) {
        logger.error("❌ ERROR [{}]: {}", context, message);
    }
}
