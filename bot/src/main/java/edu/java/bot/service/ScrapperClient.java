package edu.java.bot.service;

import edu.java.bot.api.exception.ResponseException;
import edu.java.bot.model.request.AddLinkRequest;
import edu.java.bot.model.request.RemoveLinkRequest;
import edu.java.bot.model.response.LinkResponse;
import edu.java.bot.model.response.ListLinksResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@Service
public class ScrapperClient {
    private final RestClient restClient;

    private final String tgChatPath = "tg-chat/{id}";
    private final String linkPath = "/links";
    private final String tgChatIdHeader = "Tg-Chat-Id";

    public ScrapperClient(
        RestClient.Builder restClientBuilder,
        @Value("${app.link.scrapper-uri}") String baseUrl
    ) {
        this.restClient = restClientBuilder.baseUrl(baseUrl).build();
    }

    public void registerChat(Long id) {
        restClient.post()
            .uri(tgChatPath, id)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                throw new ResponseException(response.getStatusCode().toString());
            })
            .toBodilessEntity();
    }

    public void deleteChat(Long id) {
        restClient.delete()
            .uri(tgChatPath, id)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                throw new ResponseException(response.getStatusCode().toString());
            })
            .toBodilessEntity();
    }

    public ListLinksResponse getLinks(Long tgChatId) {
        return restClient.get()
               .uri(linkPath)
               .header(tgChatIdHeader, String.valueOf(tgChatId))
               .retrieve()
               .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                  throw new ResponseException(response.getStatusCode().toString());
               })
               .body(ListLinksResponse.class);
    }

    public LinkResponse addLink(Long tgChatId, AddLinkRequest linkRequest) {
        return restClient.post()
            .uri(linkPath)
            .header(tgChatIdHeader, String.valueOf(tgChatId))
            .contentType(APPLICATION_JSON)
            .body(linkRequest)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                throw new ResponseException(response.getStatusCode().toString());
            })
            .body(LinkResponse.class);
    }

    public LinkResponse deleteLink(Long tgChatId, RemoveLinkRequest linkRequest) {
        return restClient.method(HttpMethod.DELETE)
            .uri(linkPath)
            .header(tgChatIdHeader, String.valueOf(tgChatId))
            .body(linkRequest)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                throw new ResponseException(response.getStatusCode().toString());
            })
            .body(LinkResponse.class);
    }
}
