package com.legacy.monolith.messaging;

import com.legacy.monolith.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MessagePublisher {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public void publishOrderCreated(Map<String, Object> orderData) {
        try {
            System.out.println("Publishing order created event: " + orderData.get("orderNumber"));
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.LEGACY_EXCHANGE,
                RabbitMQConfig.LEGACY_ROUTING_KEY,
                orderData
            );
            System.out.println("Order event published successfully");
        } catch (Exception e) {
            System.err.println("Failed to publish order event: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void publishGenericMessage(String exchange, String routingKey, Object message) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
        } catch (Exception e) {
            System.err.println("Failed to publish message: " + e.getMessage());
        }
    }
}