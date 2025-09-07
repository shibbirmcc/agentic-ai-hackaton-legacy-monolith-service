package com.legacy.monolith.entity;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class OrderTest {
    
    @Test
    public void testOrderCreation() {
        User user = new User("testuser", "test@example.com", "Test User");
        user.setId(1L);
        
        Order order = new Order("ORD-2024-001", user, "Test Product", 2, new BigDecimal("99.99"));
        
        assertEquals("ORD-2024-001", order.getOrderNumber());
        assertEquals(user, order.getUser());
        assertEquals("Test Product", order.getProductName());
        assertEquals(Integer.valueOf(2), order.getQuantity());
        assertEquals(new BigDecimal("99.99"), order.getPrice());
        assertEquals("PENDING", order.getOrderStatus());
        assertNotNull(order.getCreatedDate());
    }
    
    @Test
    public void testOrderSetters() {
        Order order = new Order();
        order.setId(1L);
        order.setOrderNumber("ORD-2024-002");
        order.setProductName("Another Product");
        order.setQuantity(3);
        order.setPrice(new BigDecimal("149.99"));
        order.setOrderStatus("COMPLETED");
        order.setNotes("Test notes");
        
        assertEquals(Long.valueOf(1), order.getId());
        assertEquals("ORD-2024-002", order.getOrderNumber());
        assertEquals("Another Product", order.getProductName());
        assertEquals(Integer.valueOf(3), order.getQuantity());
        assertEquals(new BigDecimal("149.99"), order.getPrice());
        assertEquals("COMPLETED", order.getOrderStatus());
        assertEquals("Test notes", order.getNotes());
    }
    
    @Test
    public void testGetUserId() {
        User user = new User("testuser", "test@example.com", "Test User");
        user.setId(123L);
        
        Order order = new Order();
        order.setUser(user);
        
        assertEquals(Long.valueOf(123), order.getUserId());
        
        Order orderWithoutUser = new Order();
        assertNull(orderWithoutUser.getUserId());
    }
}