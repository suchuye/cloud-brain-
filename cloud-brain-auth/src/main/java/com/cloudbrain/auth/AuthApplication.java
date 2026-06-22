package com.cloudbrain.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * 认证授权微服务入口。
 * 负责用户登录认证、JWT 签发、令牌刷新和二次密码验证。
 * 发布 UserLoggedInEvent / PermissionChangedEvent 供下游安全审计。
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableKafka
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
