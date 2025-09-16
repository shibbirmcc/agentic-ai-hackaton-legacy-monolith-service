package com.legacy.monolith.service;

import com.legacy.monolith.entity.Order;
import com.legacy.monolith.entity.User;
import com.legacy.monolith.messaging.MessagePublisher;
import com.legacy.monolith.repository.OrderRepository;
import com.legacy.monolith.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
        
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        
        Order result = orderService.createOrder(order);
        
        assertNotNull(result);
        assertEquals("Test Product", result.getProductName());
        verify(messagePublisher, times(1)).publishOrderCreated(any());
    }
    
    @Test
    public void testCreateOrderWithoutUser() {
        assertThrows(RuntimeException.class, () -> {
        Order order = new Order();
        order.setProductName("Test Product");
        
        orderService.createOrder(order);
        });
    }
    
    @Test
    public void testGetOrdersByUserId() {
        List<Order> orders = Arrays.asList(
            new Order(),
            new Order()
        );
        
        when(orderRepository.findByUser_Id(1L)).thenReturn(orders);
        
        List<Order> result = orderService.getOrdersByUserId(1L);
        
        assertEquals(2, result.size());
    }
}