package net.serg.resourceservice.config;

import com.amazonaws.services.proton.model.ServiceInstance;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfiguration {

    private final LoadBalancerClient loadBalancerClient;

    @Bean
    @Profile({"local-eureka", "compose-eureka"})
    public RestTemplate eurekaRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        LoadBalancerInterceptor loadBalancerInterceptor = new LoadBalancerInterceptor(loadBalancerClient);
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>(restTemplateBuilder.build().getInterceptors());
        interceptors.add(loadBalancerInterceptor);
        return restTemplateBuilder
            .interceptors(interceptors)
            .build();
    }
    
    @Bean
    @Profile("!local-eureka & !compose-eureka")
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }
    
}
