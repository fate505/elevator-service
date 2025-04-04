package com.mt.interview.elevator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableConfigurationProperties
@SpringBootApplication
public class ElevatorServiceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ElevatorServiceApplication.class, args);
    }

}
