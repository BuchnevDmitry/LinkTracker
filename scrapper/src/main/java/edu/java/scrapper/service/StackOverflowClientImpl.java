package edu.java.scrapper.service;

import edu.java.scrapper.model.QuestionRequest;
import edu.java.scrapper.model.QuestionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowClientImpl {

    private final WebClient webClient;

    public StackOverflowClientImpl(
        WebClient.Builder webClientBuilder,
        @Value("${app.link.stack-overflow}")
        String baseUrl
    ) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

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
