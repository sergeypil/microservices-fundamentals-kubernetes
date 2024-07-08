package net.serg.resourceprocessor.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    @LoadBalanced
    @Profile({"local-eureka", "compose-eureka"})
    public RestTemplate songServiceRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Profile("!local-eureka & !compose-eureka")
    public RestTemplate eurekaSongServiceRestTemplate() {
        return new RestTemplate();
    }

}
