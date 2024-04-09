package edu.java.bot.client;

import edu.java.bot.api.exception.InternalServerErrorException;
import edu.java.bot.api.exception.ResponseException;
import edu.java.bot.model.request.AddChatRequest;
import edu.java.bot.model.request.AddLinkRequest;
import edu.java.bot.model.request.RemoveLinkRequest;
import edu.java.bot.model.response.ApiErrorResponse;
import edu.java.bot.model.response.LinkResponse;
import edu.java.bot.model.response.ListLinksResponse;
import java.time.Duration;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@Slf4j
@SuppressWarnings("MagicNumber")
public class ScrapperClient {
    private final WebClient webClient;

    private final RetryPolicy retryPolicy;
    private final String tgChatPath = "tg-chat/{id}";
    private final String linkPath = "/links/{tgChatId}";

    public ScrapperClient(
        WebClient.Builder restClientBuilder,
        @Value("${app.scrapper-uri}") String baseUrl,
        RetryPolicy retryPolicy
    ) {
        this.webClient = restClientBuilder.filter(errorHandlingFilter()).baseUrl(baseUrl).build();
        this.retryPolicy = retryPolicy;
    }

    public void registerChat(Long id, AddChatRequest chatRequest) {
        webClient.post()
            .uri(tgChatPath, id)
            .contentType(APPLICATION_JSON)
            .body(Mono.just(chatRequest), AddChatRequest.class)
            .retrieve()
            .toBodilessEntity()
            .retryWhen(retryPolicy.constant(Duration.ofSeconds(2), 3, List.of(InternalServerErrorException.class)))
            .block();
    }

    public void deleteChat(Long id) {
        webClient.delete()
            .uri(tgChatPath, id)
            .retrieve()
            .toBodilessEntity()
            .retryWhen(retryPolicy.linear(Duration.ofSeconds(2), 3, List.of(InternalServerErrorException.class)))
            .block();
    }

    public ListLinksResponse getLinks(Long tgChatId) {
        return webClient.get()
            .uri(linkPath, tgChatId)
            .retrieve()
            .bodyToMono(ListLinksResponse.class)
            .retryWhen(retryPolicy.constant(Duration.ofSeconds(2), 3, List.of(InternalServerErrorException.class)))
            .block();
    }

    public LinkResponse addLink(Long tgChatId, AddLinkRequest linkRequest) {
        return webClient.post()
            .uri(linkPath, tgChatId)
            .contentType(APPLICATION_JSON)
            .body(Mono.just(linkRequest), AddLinkRequest.class)
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .retryWhen(retryPolicy.constant(Duration.ofSeconds(2), 3, List.of(InternalServerErrorException.class)))
            .block();
    }

    public LinkResponse deleteLink(Long tgChatId, RemoveLinkRequest linkRequest) {
        return webClient.method(HttpMethod.DELETE)
            .uri(linkPath, tgChatId)
            .body(Mono.just(linkRequest), RemoveLinkRequest.class)
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .retryWhen(retryPolicy.constant(Duration.ofSeconds(2), 3, List.of(InternalServerErrorException.class)))
            .block();
    }

    private ExchangeFilterFunction errorHandlingFilter() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (clientResponse.statusCode() == null) {
                return Mono.just(clientResponse);
            }
            if (clientResponse.statusCode().is5xxServerError()) {
                return handle5xxError();
            } else if (clientResponse.statusCode().is4xxClientError()) {
                return handle4xxError(clientResponse);
            }
            return Mono.just(clientResponse);
        });
    }

    private Mono<ClientResponse> handle4xxError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(ApiErrorResponse.class)
            .flatMap(errorBody -> {
                String method = clientResponse.request().getMethod().toString();
                String path = clientResponse.request().getURI().getPath();
                log.error(String.format(
                    "При выполнении операции {%s} {%s} возникла ошибка: {%s}",
                    method,
                    path,
                    errorBody.description()
                ));
                return Mono.error(new ResponseException(errorBody.description()));
            });
    }

    private Mono<ClientResponse> handle5xxError() {
        return Mono.error(new InternalServerErrorException("Internal Server Error"));
    }

}
