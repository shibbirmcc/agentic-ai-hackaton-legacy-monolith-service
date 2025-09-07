package com.legacy.monolith.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    public static final String LEGACY_EXCHANGE = "legacy.exchange";
    public static final String LEGACY_QUEUE = "legacy.order.queue";
    public static final String LEGACY_ROUTING_KEY = "legacy.order.created";
    
    @Bean
    public DirectExchange legacyExchange() {
        return new DirectExchange(LEGACY_EXCHANGE, true, false);
    }
    
    @Bean
    public Queue legacyQueue() {
        return new Queue(LEGACY_QUEUE, true);
    }
    
    @Bean
    public Binding legacyBinding(Queue legacyQueue, DirectExchange legacyExchange) {
        return BindingBuilder.bind(legacyQueue).to(legacyExchange).with(LEGACY_ROUTING_KEY);
    }
    
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}