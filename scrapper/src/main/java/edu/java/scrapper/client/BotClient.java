package edu.java.scrapper.client;

import edu.java.scrapper.api.exception.BadRequestException;
import edu.java.scrapper.model.request.LinkUpdateRequest;
import edu.java.scrapper.model.response.ApiErrorResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class BotClient {

    private final WebClient webClient;

    public BotClient(
        WebClient.Builder webClientBuilder,
        @Value("${app.link.bot-uri}") String baseUrl
    ) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public void addUpdate(LinkUpdateRequest linkRequest) {
        webClient.post()
            .uri("/updates")
            .contentType(APPLICATION_JSON)
            .body(Mono.just(linkRequest), LinkUpdateRequest.class)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(ApiErrorResponse.class)
                .flatMap(apiErrorResponse -> Mono.error(new BadRequestException(apiErrorResponse.description()))))
            .toBodilessEntity()
            .block();
    }

}
