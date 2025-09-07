package com.legacy.monolith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class LegacyMonolithApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(LegacyMonolithApplication.class, args);
        System.out.println("========================================");
        System.out.println("Legacy Monolith Service Started");
        System.out.println("Running on: http://localhost:8080");
        System.out.println("Health check: http://localhost:8080/health");
        System.out.println("========================================");
    }
}