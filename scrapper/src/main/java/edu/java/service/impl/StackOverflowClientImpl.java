package edu.java.service.impl;

import edu.java.model.QuestionRequest;
import edu.java.model.QuestionResponse;
import edu.java.model.RepositoryResponse;
import edu.java.service.StackOverflowClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowClientImpl implements StackOverflowClient {

    private final WebClient webClient;

    public StackOverflowClientImpl(
        WebClient.Builder webClientBuilder,
        @Value("https://api.stackexchange.com/2.3/questions/")
        String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }
    @Override
    public QuestionResponse fetchQuestion(QuestionRequest request) {
        return this.webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path(String.format("%s", request.number()))
                .queryParam("order", request.order())
                .queryParam("sort", request.sort())
                .queryParam("site", request.site())
                .build())
            .retrieve().bodyToMono(QuestionResponse.class)
            .block();
    }
}
