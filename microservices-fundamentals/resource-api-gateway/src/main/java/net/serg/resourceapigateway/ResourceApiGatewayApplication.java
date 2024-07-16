package net.serg.resourceapigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@SpringBootApplication
@EnableWebFluxSecurity
public class ResourceApiGatewayApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(ResourceApiGatewayApplication.class, args);
		run.getBean(SecurityWebFilterChain.class).getWebFilters().subscribe(System.out::println);
		System.out.println(run);
	}

}
