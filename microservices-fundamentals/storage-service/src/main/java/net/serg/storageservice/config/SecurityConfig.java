package net.serg.storageservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/storages/**")
            .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(
                jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(customJwtAuthenticationConverter())
            ));
        return http.build();
    }

    @Bean
    public Converter<Jwt, JwtAuthenticationToken> customJwtAuthenticationConverter() {
        return new CustomJwtAuthenticationConverter();
    }
}
