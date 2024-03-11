package edu.java.bot.client;

import edu.java.bot.api.exception.ResponseException;
import edu.java.bot.model.request.AddLinkRequest;
import edu.java.bot.model.request.RemoveLinkRequest;
import edu.java.bot.model.response.ApiErrorResponse;
import edu.java.bot.model.response.LinkResponse;
import edu.java.bot.model.response.ListLinksResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@Slf4j
public class ScrapperClient {
    private final WebClient webClient;
    private final String tgChatPath = "tg-chat/{id}";
    private final String linkPath = "/links/{tgChatId}";

    public ScrapperClient(
        WebClient.Builder restClientBuilder,
        @Value("${app.scrapper-uri}") String baseUrl
    ) {
        this.webClient = restClientBuilder.filter(errorHandlingFilter()).baseUrl(baseUrl).build();
    }

    public void registerChat(Long id) {
        webClient.post()
            .uri(tgChatPath, id)
            .retrieve()
            .toBodilessEntity()
            .block();
    }

    public void deleteChat(Long id) {
        webClient.delete()
            .uri(tgChatPath, id)
            .retrieve()
            .toBodilessEntity()
            .block();
    }

    public ListLinksResponse getLinks(Long tgChatId) {
        return webClient.get()
            .uri(linkPath, tgChatId)
            .retrieve()
            .bodyToMono(ListLinksResponse.class)
            .block();
    }

    public LinkResponse addLink(Long tgChatId, AddLinkRequest linkRequest) {
        return webClient.post()
            .uri(linkPath, tgChatId)
            .contentType(APPLICATION_JSON)
            .body(Mono.just(linkRequest), AddLinkRequest.class)
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .block();
    }

    public LinkResponse deleteLink(Long tgChatId, RemoveLinkRequest linkRequest) {
        return webClient.method(HttpMethod.DELETE)
            .uri(linkPath, tgChatId)
            .body(Mono.just(linkRequest), RemoveLinkRequest.class)
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .block();
    }

    private ExchangeFilterFunction errorHandlingFilter() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (clientResponse.statusCode() != null
                && (clientResponse.statusCode().is5xxServerError() || clientResponse.statusCode().is4xxClientError())) {
                return clientResponse.bodyToMono(ApiErrorResponse.class)
                    .flatMap(errorBody -> {
                        return Mono.error(new ResponseException(errorBody.description()));
                    });
            }
            return Mono.just(clientResponse);
        });
    }
}
