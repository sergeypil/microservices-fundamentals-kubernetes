package net.serg.resourceprocessor.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Bean
    Queue resourceIdsQueue() {
        return new Queue("resourceIds");
    }
}