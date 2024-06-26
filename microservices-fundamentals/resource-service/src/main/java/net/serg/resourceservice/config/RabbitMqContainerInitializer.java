package net.serg.resourceservice.config;

import org.testcontainers.containers.RabbitMQContainer;

public class RabbitMqContainerInitializer {

    public static RabbitMQContainer initialize() {
        RabbitMQContainer container = new RabbitMQContainer("rabbitmq:3-management")
            .withExposedPorts(5672, 15672);

        container.start();

        System.setProperty("spring.rabbitmq.host", container.getContainerIpAddress());
        System.setProperty("spring.rabbitmq.port", container.getMappedPort(5672).toString());

        return container;
    }
}