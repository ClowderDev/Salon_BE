package com.clowder.booking.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String BOOKING_QUEUE       = "booking-queue";
    public static final String BOOKING_DLQ         = "booking-dead-queue";
    public static final String BOOKING_DLX         = "booking-dlx";
    public static final String BOOKING_DLX_ROUTING = "booking-dead";

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter converter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter);
        return rabbitTemplate;
    }

    // Main queue — routes failures to DLX after exhausting retries
    @Bean
    public Queue bookingQueue() {
        return QueueBuilder.durable(BOOKING_QUEUE)
                .withArgument("x-dead-letter-exchange", BOOKING_DLX)
                .withArgument("x-dead-letter-routing-key", BOOKING_DLX_ROUTING)
                .build();
    }

    // Dead-letter exchange
    @Bean
    public DirectExchange bookingDeadLetterExchange() {
        return new DirectExchange(BOOKING_DLX);
    }

    // Dead-letter queue — parks failed messages for inspection
    @Bean
    public Queue bookingDeadLetterQueue() {
        return QueueBuilder.durable(BOOKING_DLQ).build();
    }

    // Bind DLQ to DLX
    @Bean
    public Binding bookingDeadLetterBinding() {
        return BindingBuilder
                .bind(bookingDeadLetterQueue())
                .to(bookingDeadLetterExchange())
                .with(BOOKING_DLX_ROUTING);
    }
}

