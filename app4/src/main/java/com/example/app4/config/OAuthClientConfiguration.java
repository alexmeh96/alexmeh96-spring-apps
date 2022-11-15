package com.example.app4.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OAuthClientConfiguration {

    @Value("${api.baseUrl}")
    public String baseUrl;

    private String token;

    @Bean
    ReactiveClientRegistrationRepository clientRegistrations(
            @Value("${spring.security.oauth2.client.provider.mehAuth.token-uri}") String token_uri,
            @Value("${spring.security.oauth2.client.registration.mehAuth.client-id}") String client_id,
            @Value("${spring.security.oauth2.client.registration.mehAuth.client-secret}") String client_secret,
            @Value("${spring.security.oauth2.client.registration.mehAuth.scope}") String scope,
            @Value("${spring.security.oauth2.client.registration.mehAuth.authorization-grant-type}") String authorizationGrantType

    ) {
        ClientRegistration registration = ClientRegistration
                .withRegistrationId("mehAuth")
                .tokenUri(token_uri)
                .clientId(client_id)
                .clientSecret(client_secret)
                .scope(scope)
                .authorizationGrantType(new AuthorizationGrantType(authorizationGrantType))
                .build();
        return new InMemoryReactiveClientRegistrationRepository(registration);
    }

    @Bean
    WebClient webClient(ReactiveClientRegistrationRepository clientRegistrations) {
        var clientService = new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrations);
        var authorizedClientManager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrations, clientService);
        var oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauth.setDefaultClientRegistrationId("mehAuth");
        return WebClient.builder()
                .filter(oauth)
                .build();
    }

    @Bean
    WebClient baseWebClient() {
        return WebClient.builder().baseUrl(baseUrl + "token").build();
    }

    @Bean
    WebClient webClientCustomFilter(WebClient baseWebClient) {
        ExchangeFilterFunction tokenCheckFilter = (clientRequest, nextFilter) -> {
            if (!StringUtils.hasText(this.token)) {
                this.token = baseWebClient
                        .post()
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON)
                        .body(
                                BodyInserters.fromFormData("grant_type", "password")
                                        .with("username", "username1")
                                        .with("password", "password1")
                        )
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
            }

            return nextFilter.exchange(withBearerAuth(clientRequest, this.token));
        };

        return WebClient.builder()
                .filter(tokenCheckFilter)
                .build();
    }


    private static ClientRequest withBearerAuth(ClientRequest request,
                                                String token) {
        return ClientRequest.from(request)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
    }
}
