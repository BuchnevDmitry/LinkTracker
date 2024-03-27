package edu.java.scrapper.client;

import edu.java.scrapper.api.exception.BadRequestException;
import edu.java.scrapper.model.request.QuestionRequest;
import edu.java.scrapper.model.response.QuestionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowClient {

    private final WebClient webClient;

    public StackOverflowClient(
        WebClient.Builder webClientBuilder,
        @Value("${app.url.stack-overflow}")
        String baseUrl
    ) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public QuestionResponse fetchQuestion(QuestionRequest request) {
        return this.webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path(String.format("%s", request.getNumber()))
                .queryParam("order", request.getOrder())
                .queryParam("sort", request.getSort())
                .queryParam("site", request.getSite())
                .build())
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response -> {
                throw new BadRequestException("Ошибка запроса информации о вопросе");
            })
            .bodyToMono(QuestionResponse.class)
            .block();
    }
}
