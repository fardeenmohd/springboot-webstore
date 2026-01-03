package com.webstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.webstore"})
public class WebstoreApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(WebstoreApplication.class);
        app.run(args);
    }
}