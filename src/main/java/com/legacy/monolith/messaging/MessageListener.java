package com.legacy.monolith.messaging;

import com.legacy.monolith.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MessageListener {
    
    @RabbitListener(queues = RabbitMQConfig.LEGACY_QUEUE)
    public void handleOrderCreated(Map<String, Object> orderData) {
        System.out.println("================================");
        System.out.println("Received order created event");
        System.out.println("Order ID: " + orderData.get("orderId"));
        System.out.println("Order Number: " + orderData.get("orderNumber"));
        System.out.println("User ID: " + orderData.get("userId"));
        System.out.println("Amount: " + orderData.get("amount"));
        System.out.println("Timestamp: " + orderData.get("timestamp"));
        System.out.println("================================");
        
        try {
            processOrderEvent(orderData);
        } catch (Exception e) {
            System.err.println("Error processing order event: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void processOrderEvent(Map<String, Object> orderData) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Processing order event for order: " + orderData.get("orderNumber"));
        
        if (orderData.get("amount") != null) {
            System.out.println("Order amount validated: " + orderData.get("amount"));
        }
        
        System.out.println("Order event processing completed");
    }
}