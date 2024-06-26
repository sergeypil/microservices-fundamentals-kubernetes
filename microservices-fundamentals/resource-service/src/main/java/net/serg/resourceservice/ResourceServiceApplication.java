package net.serg.resourceservice;

import net.serg.resourceservice.config.RabbitMqContainerInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ResourceServiceApplication {
	public static void main(String[] args) {
		//RabbitMqContainerInitializer.initialize();
		SpringApplication.run(ResourceServiceApplication.class, args);
	}
}
