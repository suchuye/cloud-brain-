package com.cloudbrain.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Spring Boot application entry point for the Admin module.
 * Provides administrative operations including schedule management, doctor listing,
 * and permission configuration.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableKafka
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
