package com.bookstorage.config;

import com.bookstorage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Starting data initialization...");
        userService.initializeDefaultAdmin();
        logger.info("✅ Default admin user initialized successfully!");
    }
}
