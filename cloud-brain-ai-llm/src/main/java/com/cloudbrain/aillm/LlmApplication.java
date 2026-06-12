package com.cloudbrain.aillm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LlmApplication {
    public static void main(String[] args) {
        SpringApplication.run(LlmApplication.class, args);
    }
}
