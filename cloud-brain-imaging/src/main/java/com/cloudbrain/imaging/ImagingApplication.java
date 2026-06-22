package com.cloudbrain.imaging;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Spring Boot entry point for the imaging microservice.
 * <p>Manages AI-powered medical image analysis including task submission,
 * preprocessing, inference, and report generation.</p>
 */
@EnableScheduling
@SpringBootApplication
@EnableDiscoveryClient
@EnableKafka
public class ImagingApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImagingApplication.class, args);
    }
}
