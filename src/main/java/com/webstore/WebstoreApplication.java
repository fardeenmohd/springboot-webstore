package com.webstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.webstore", "com.users"})
public class WebstoreApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(WebstoreApplication.class);
        app.setAdditionalProfiles("webstore");
        app.run(args);
    }
}