package edu.java.scrapper.client;

import edu.java.scrapper.api.exception.BadRequestException;
import edu.java.scrapper.api.exception.InternalServerErrorException;
import edu.java.scrapper.model.request.QuestionRequest;
import edu.java.scrapper.model.response.AnswerResponse;
import edu.java.scrapper.model.response.QuestionResponse;
import java.time.Duration;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@SuppressWarnings("MagicNumber")
@Component
public class StackOverflowClient {

    private final WebClient webClient;

    private String order = "order";
    private String sort = "sort";
    private String site = "site";

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
                .queryParam(order, request.getOrder())
                .queryParam(sort, request.getSort())
                .queryParam(site, request.getSite())
                .build())
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response -> {
                throw new BadRequestException("Ошибка запроса информации o вопросе");
            })
            .onStatus(HttpStatusCode::is5xxServerError, response -> {
                throw new InternalServerErrorException("Ошибка сервера при запросе информации o вопросе");
            })
            .bodyToMono(QuestionResponse.class)
            .retryWhen(RetryUtil.exponential(Duration.ofSeconds(2), 3, List.of(InternalServerErrorException.class)))
            .block();
    }

    public AnswerResponse fetchQuestionAnswer(QuestionRequest request) {
        return this.webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path(String.format("%s/answers", request.getNumber()))
                .queryParam(order, request.getOrder())
                .queryParam(sort, request.getSort())
                .queryParam(site, request.getSite())
                .build())
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response -> {
                throw new BadRequestException("Ошибка запроса информации об ответах на вопрос");
            })
            .onStatus(HttpStatusCode::is5xxServerError, response -> {
                throw new InternalServerErrorException("""
                    Ошибка сервера при запросе информации oб ответах на вопрос вопросе
                    """);
            })
            .bodyToMono(AnswerResponse.class)
            .retryWhen(RetryUtil.linear(Duration.ofSeconds(2), 4, List.of(InternalServerErrorException.class)))
            .block();
    }
}
