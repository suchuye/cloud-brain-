package com.cloudbrain.outpatient;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Spring Boot application entry point for the outpatient module.
 * Provides consultation management, EMR, and event-driven integration with other modules.
 */
@EnableScheduling
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableKafka
public class OutpatientApplication {
    public static void main(String[] args) {
        SpringApplication.run(OutpatientApplication.class, args);
    }
}
