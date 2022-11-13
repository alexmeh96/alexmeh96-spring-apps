package com.example.app3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ApiService {

    private final WebClient webClient;

    @Value("${api.baseUrl}")
    public String baseUrl;

    public Mono<String> getFromApi() {

        return webClient
                .get()
                .uri(baseUrl)
                .retrieve()
                .bodyToMono(String.class);
    }

}
