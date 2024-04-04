package edu.java.scrapper.client;

import edu.java.scrapper.api.exception.BadRequestException;
import edu.java.scrapper.api.exception.InternalServerErrorException;
import edu.java.scrapper.model.request.LinkUpdateRequest;
import edu.java.scrapper.model.response.ApiErrorResponse;
import java.time.Duration;
import java.util.List;
import edu.java.scrapper.service.Updater;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@SuppressWarnings("MagicNumber")
public class BotClient implements Updater {

    private final WebClient webClient;

    private final RetryPolicy retryPolicy;

    public BotClient(
        WebClient.Builder webClientBuilder,
        @Value("${app.link.bot-uri}") String baseUrl,
        RetryPolicy retryPolicy
    ) {
        this.retryPolicy = retryPolicy;
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public void sendLinkUpdate(LinkUpdateRequest update) {
        webClient.post()
            .uri("/updates")
            .contentType(APPLICATION_JSON)
            .body(Mono.just(update), LinkUpdateRequest.class)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(ApiErrorResponse.class)
                .flatMap(apiErrorResponse -> Mono.error(new BadRequestException(apiErrorResponse.description()))))
            .onStatus(HttpStatusCode::is5xxServerError, response -> {
                throw new InternalServerErrorException("Ошибка сервера при запросе информации o вопросе");
            })
            .toBodilessEntity()
            .retryWhen(retryPolicy.linear(Duration.ofSeconds(2), 6, List.of(InternalServerErrorException.class)))
            .block();
    }

}
