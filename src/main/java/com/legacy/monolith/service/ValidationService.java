package com.legacy.monolith.service;

import org.springframework.stereotype.Service;

@Service
public class ValidationService {
    
    public boolean validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.contains("@") && email.contains(".");
    }
    
    public boolean validateUsername(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        return username.length() >= 3 && username.length() <= 50;
    }
    
    public boolean validateOrderNumber(String orderNumber) {
        if (orderNumber == null || orderNumber.isEmpty()) {
            return false;
        }
        return orderNumber.matches("ORD-\\d{4}-\\d{5}");
    }
    
    public void performLegacyValidation(Object obj) {
        System.out.println("Performing legacy validation for: " + obj.getClass().getSimpleName());
    }
}