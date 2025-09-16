package com.legacy.monolith.messaging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessagePublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private MessagePublisher messagePublisher;

    @Test
    void testPublishOrderCreated() {
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("orderNumber", "ORD-123");
        orderData.put("userId", 1L);

        messagePublisher.publishOrderCreated(orderData);

        verify(rabbitTemplate).convertAndSend(anyString(), anyString(), eq(orderData));
    }

    @Test
    void testPublishGenericMessage() {
        String exchange = "test.exchange";
        String routingKey = "test.key";
        Object message = "test message";

        messagePublisher.publishGenericMessage(exchange, routingKey, message);

        verify(rabbitTemplate).convertAndSend(exchange, routingKey, message);
    }
}
