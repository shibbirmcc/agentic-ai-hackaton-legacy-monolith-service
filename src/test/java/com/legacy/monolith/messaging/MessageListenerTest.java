package com.legacy.monolith.messaging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class MessageListenerTest {

    @InjectMocks
    private MessageListener messageListener;

    @Test
    void testHandleOrderCreated() {
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("orderId", 1L);
        orderData.put("orderNumber", "ORD-123");
        orderData.put("userId", 1L);
        orderData.put("amount", 100.0);
        orderData.put("timestamp", System.currentTimeMillis());

        assertDoesNotThrow(() -> messageListener.handleOrderCreated(orderData));
    }

    @Test
    void testHandleOrderCreatedWithNullAmount() {
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("orderId", 1L);
        orderData.put("orderNumber", "ORD-123");
        orderData.put("userId", 1L);
        orderData.put("amount", null);

        assertDoesNotThrow(() -> messageListener.handleOrderCreated(orderData));
    }
}
