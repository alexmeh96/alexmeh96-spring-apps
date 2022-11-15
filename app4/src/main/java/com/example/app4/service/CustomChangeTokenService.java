package com.example.app4.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomChangeTokenService {

    private final WebClient webClientCustomFilter;

    @Value("${api.baseUrl}")
    public String baseUrl;

    public Mono<String> getFromApi() {

        return webClientCustomFilter
                .get()
                .uri(baseUrl)
                .retrieve()
                .bodyToMono(String.class);
    }

}
