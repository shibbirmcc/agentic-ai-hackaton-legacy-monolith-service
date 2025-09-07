package com.legacy.monolith.service;

import com.legacy.monolith.entity.Order;
import com.legacy.monolith.entity.User;
import com.legacy.monolith.messaging.MessagePublisher;
import com.legacy.monolith.repository.OrderRepository;
import com.legacy.monolith.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {
    
    @Mock
    private OrderRepository orderRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private MessagePublisher messagePublisher;
    
    @Mock
    private ValidationService validationService;
    
    @InjectMocks
    private OrderService orderService;
    
    @Test
    public void testCreateOrder() {
        User user = new User("testuser", "test@example.com", "Test User");
        user.setId(1L);
        
        Order order = new Order();
        order.setUser(user);
        order.setProductName("Test Product");
        order.setQuantity(2);
        order.setPrice(new BigDecimal("99.99"));
        
        when(userRepository.findOne(1L)).thenReturn(user);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        
        Order result = orderService.createOrder(order);
        
        assertNotNull(result);
        assertEquals("Test Product", result.getProductName());
        verify(messagePublisher, times(1)).publishOrderCreated(any());
    }
    
    @Test(expected = RuntimeException.class)
    public void testCreateOrderWithoutUser() {
        Order order = new Order();
        order.setProductName("Test Product");
        
        orderService.createOrder(order);
    }
    
    @Test
    public void testGetOrdersByUserId() {
        List<Order> orders = Arrays.asList(
            new Order(),
            new Order()
        );
        
        when(orderRepository.findByUserId(1L)).thenReturn(orders);
        
        List<Order> result = orderService.getOrdersByUserId(1L);
        
        assertEquals(2, result.size());
    }
}