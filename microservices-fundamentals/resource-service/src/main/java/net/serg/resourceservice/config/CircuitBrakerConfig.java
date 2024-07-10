package net.serg.resourceservice.config;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

//@Configuration
public class CircuitBrakerConfig {
//    
//    @Bean
//    public CircuitBreaker countCircuitBreaker() {
//        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
//                                                                        .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
//                                                                        .slidingWindowSize(1)
//                                                                        .minimumNumberOfCalls(1)
//                                                                        .failureRateThreshold(100)
//                                                                        .build();
//        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
//        var circuitBreaker = circuitBreakerRegistry.circuitBreaker("StoreApiBasedOnCount");
//        circuitBreaker.getEventPublisher().onStateTransition(s -> {
//            LoggerFactory
//                .getLogger("some").info(s.toString());
//        });
//        return circuitBreaker;
//    }
}
