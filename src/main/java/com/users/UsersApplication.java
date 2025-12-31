package com.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.users"})
public class UsersApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(UsersApplication.class);
        app.setAdditionalProfiles("users");
        app.run(args);
    }
}
