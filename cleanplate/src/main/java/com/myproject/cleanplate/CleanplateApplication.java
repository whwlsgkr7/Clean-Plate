package com.myproject.cleanplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CleanplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(CleanplateApplication.class, args);
    }

}
