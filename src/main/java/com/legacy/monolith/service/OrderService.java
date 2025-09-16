package com.legacy.monolith.service;

import com.legacy.monolith.entity.Order;
import com.legacy.monolith.entity.User;
import com.legacy.monolith.messaging.MessagePublisher;
import com.legacy.monolith.repository.OrderRepository;
import com.legacy.monolith.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MessagePublisher messagePublisher;
    
    @Autowired
    private ValidationService validationService;
    
    private static int orderCounter = 1000;
    
    public Order createOrder(Order order) {
        Long userId = order.getUserId();
        if (userId == null && order.getUser() == null) {
            throw new RuntimeException("User ID is required for order");
        }
        
        if (userId != null) {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
            order.setUser(user);
        }
        
        if (order.getOrderNumber() == null || order.getOrderNumber().isEmpty()) {
            order.setOrderNumber(generateOrderNumber());
        }
        
        if (order.getQuantity() == null || order.getQuantity() <= 0) {
            order.setQuantity(1);
        }
        
        if (order.getPrice() == null) {
            order.setPrice(BigDecimal.ZERO);
        }
        
        order.setCreatedDate(new Date());
        order.setOrderStatus("PENDING");
        
        Order savedOrder = orderRepository.save(order);
        
        try {
            sendOrderCreatedMessage(savedOrder);
        } catch (Exception e) {
            System.err.println("Failed to send message: " + e.getMessage());
        }
        
        calculateOrderTotal(savedOrder);
        validateOrderData(savedOrder);
        performOrderValidation(savedOrder);
        
        System.out.println("Order created: " + savedOrder.getOrderNumber());
        
        return savedOrder;
    }
    
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }
    
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUser_Id(userId);
    }
    
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    public Order updateOrderStatus(Long id, String status) {
        Order order = getOrderById(id);
        order.setOrderStatus(status);
        return orderRepository.save(order);
    }
    
    private String generateOrderNumber() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String year = sdf.format(new Date());
        return "ORD-" + year + "-" + "%05d".formatted(orderCounter++);
    }
    
    private void sendOrderCreatedMessage(Order order) {
        Map<String, Object> message = new HashMap<>();
        message.put("orderId", order.getId());
        message.put("orderNumber", order.getOrderNumber());
        message.put("userId", order.getUserId());
        message.put("amount", order.getPrice());
        message.put("timestamp", new Date());
        
        messagePublisher.publishOrderCreated(message);
    }
    
    private BigDecimal calculateOrderTotal(Order order) {
        if (order.getPrice() != null && order.getQuantity() != null) {
            return order.getPrice().multiply(BigDecimal.valueOf(order.getQuantity()));
        }
        return BigDecimal.ZERO;
    }
    
    private boolean validateOrderData(Order order) {
        if (order == null) return false;
        if (order.getProductName() == null || order.getProductName().isEmpty()) return false;
        if (order.getQuantity() == null || order.getQuantity() <= 0) return false;
        if (order.getPrice() == null || order.getPrice().compareTo(BigDecimal.ZERO) < 0) return false;
        return true;
    }
    
    private void performOrderValidation(Order order) {
        if (order.getPrice().compareTo(new BigDecimal("10000")) > 0) {
            System.out.println("High value order detected: " + order.getOrderNumber());
        }
        
        if (order.getQuantity() > 100) {
            System.out.println("Bulk order detected: " + order.getOrderNumber());
        }
    }
    
    public void processOrderBatch() {
        List<Order> pendingOrders = orderRepository.findByOrderStatus("PENDING");
        for (Order order : pendingOrders) {
            if (order.getCreatedDate().getTime() < System.currentTimeMillis() - 86400000) {
                order.setOrderStatus("EXPIRED");
                orderRepository.save(order);
            }
        }
    }
}