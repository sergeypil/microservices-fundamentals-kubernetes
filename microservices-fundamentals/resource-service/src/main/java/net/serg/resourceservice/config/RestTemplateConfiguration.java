package net.serg.resourceservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.hateoas.config.HypermediaRestTemplateConfigurer;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static java.util.Collections.singletonList;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    @LoadBalanced
    @Profile({"local-eureka", "compose-eureka"})
    public RestTemplate storageServiceRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Profile("!local-eureka & !compose-eureka")
    public RestTemplate eurekaStorageServiceRestTemplate() {
        return new RestTemplate();
    }
}
