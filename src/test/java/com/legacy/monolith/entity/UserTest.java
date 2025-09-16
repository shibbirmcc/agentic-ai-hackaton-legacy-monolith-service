package com.legacy.monolith.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    
    @Test
    public void testUserCreation() {
        User user = new User("testuser", "test@example.com", "Test User");
        
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Test User", user.getFullName());
        assertEquals("ACTIVE", user.getStatus());
        assertNotNull(user.getCreatedDate());
    }
    
    @Test
    public void testUserSetters() {
        User user = new User();
        user.setId(1L);
        user.setUsername("newuser");
        user.setEmail("new@example.com");
        user.setFullName("New User");
        user.setStatus("INACTIVE");
        
        assertEquals(Long.valueOf(1), user.getId());
        assertEquals("newuser", user.getUsername());
        assertEquals("new@example.com", user.getEmail());
        assertEquals("New User", user.getFullName());
        assertEquals("INACTIVE", user.getStatus());
    }
}