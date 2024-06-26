package net.serg.resourceservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMqSender {

    private final RabbitTemplate rabbitTemplate;
    
    public void send(String queue, String message) {
        rabbitTemplate.convertAndSend(queue, message);
    }
}