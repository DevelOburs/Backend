package com.fridgify.api_gateway.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/")
public class GatewayController {

    @Value("${api-urls.auth-api}")
    private String authApiUrl;

    @Value("${api-urls.user-api}")
    private String userApiUrl;

    @Value("${api-urls.recipe-api}")
    private String recipeApiUrl;

    private final WebClient webClient;

    public GatewayController() {
        this.webClient = WebClient.create();
    }

    @GetMapping("/wake-up")
    public Mono<ResponseEntity<String>> wakeUp() {
        Mono<String> userResponse = webClient.get().uri(userApiUrl)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(300))
                .onErrorResume(e -> Mono.just("User API Error: " + e.getMessage()));

        Mono<String> authResponse = webClient.get().uri(authApiUrl)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(300))
                .onErrorResume(e -> Mono.just("Auth API Error: " + e.getMessage()));

        Mono<String> recipeResponse = webClient.get().uri(recipeApiUrl)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(300))
                .onErrorResume(e -> Mono.just("Recipe API Error: " + e.getMessage()));

        return Mono.zip(userResponse, authResponse, recipeResponse)
                .map(tuple -> {
                    String combinedResponse = "User API: " + tuple.getT1() + "\n"
                            + "Auth API: " + tuple.getT2() + "\n"
                            + "Recipe API: " + tuple.getT3() + "\n";
                    return ResponseEntity.status(HttpStatus.OK)
                            .header("Content-Type", "text/plain")
                            .body(combinedResponse);
                });
    }
}
