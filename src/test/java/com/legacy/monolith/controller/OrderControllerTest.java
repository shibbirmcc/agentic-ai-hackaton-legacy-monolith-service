package com.legacy.monolith.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.legacy.monolith.entity.Order;
import com.legacy.monolith.entity.User;
import com.legacy.monolith.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {
    
    private MockMvc mockMvc;
    
    @Mock
    private OrderService orderService;
    
    @InjectMocks
    private OrderController orderController;
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }
    
    @Test
    public void testCreateOrder() throws Exception {
        User user = new User("testuser", "test@example.com", "Test User");
        user.setId(1L);
        
        Order order = new Order("ORD-2024-001", user, "Test Product", 2, new BigDecimal("99.99"));
        order.setId(1L);
        
        when(orderService.createOrder(any(Order.class))).thenReturn(order);
        
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":1,\"productName\":\"Test Product\",\"quantity\":2,\"price\":99.99}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderNumber").value("ORD-2024-001"));
    }
    
    @Test
    public void testGetOrderById() throws Exception {
        User user = new User("testuser", "test@example.com", "Test User");
        user.setId(1L);
        
        Order order = new Order("ORD-2024-001", user, "Test Product", 2, new BigDecimal("99.99"));
        order.setId(1L);
        
        when(orderService.getOrderById(1L)).thenReturn(order);
        
        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNumber").value("ORD-2024-001"));
    }
    
    @Test
    public void testGetOrdersByUserId() throws Exception {
        User user = new User("testuser", "test@example.com", "Test User");
        user.setId(1L);
        
        List<Order> orders = Arrays.asList(
            new Order("ORD-2024-001", user, "Product 1", 1, new BigDecimal("50.00")),
            new Order("ORD-2024-002", user, "Product 2", 2, new BigDecimal("75.00"))
        );
        
        when(orderService.getOrdersByUserId(1L)).thenReturn(orders);
        
        mockMvc.perform(get("/orders?userId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderNumber").value("ORD-2024-001"))
                .andExpect(jsonPath("$[1].orderNumber").value("ORD-2024-002"));
    }
}