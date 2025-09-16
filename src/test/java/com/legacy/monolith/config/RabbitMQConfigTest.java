package com.legacy.monolith.config;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;

import static org.junit.jupiter.api.Assertions.*;

class RabbitMQConfigTest {

    private final RabbitMQConfig config = new RabbitMQConfig();

    @Test
    void testLegacyQueue() {
        Queue queue = config.legacyQueue();
        assertNotNull(queue);
        assertEquals(RabbitMQConfig.LEGACY_QUEUE, queue.getName());
        assertTrue(queue.isDurable());
    }

    @Test
    void testLegacyExchange() {
        DirectExchange exchange = config.legacyExchange();
        assertNotNull(exchange);
        assertEquals(RabbitMQConfig.LEGACY_EXCHANGE, exchange.getName());
        assertTrue(exchange.isDurable());
    }

    @Test
    void testConstants() {
        assertEquals("legacy.order.queue", RabbitMQConfig.LEGACY_QUEUE);
        assertEquals("legacy.exchange", RabbitMQConfig.LEGACY_EXCHANGE);
        assertEquals("legacy.order.created", RabbitMQConfig.LEGACY_ROUTING_KEY);
    }
}
