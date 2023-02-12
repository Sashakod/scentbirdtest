package com.example.scentbirdtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ScentbirdTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScentbirdTestApplication.class, args);
    }
}
