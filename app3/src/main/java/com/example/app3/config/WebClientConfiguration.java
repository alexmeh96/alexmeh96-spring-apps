package com.example.app3.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @Bean
    public WebClient getWebClient(@Value("${api.baseUrl}") String baseUrl) {
        return WebClient.create(baseUrl);
    }
}
