package com.bookstorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OnlineLibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineLibraryApplication.class, args);
    }
}
