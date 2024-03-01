package edu.java.scrapper.service;

import edu.java.scrapper.api.exception.ResponseException;
import edu.java.scrapper.model.request.LinkUpdateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class BotClient {

    private final RestClient restClient;

    public BotClient(
        RestClient.Builder restClientBuilder,
        @Value("${app.link.bot-uri}") String baseUrl
    ) {
        this.restClient = restClientBuilder.baseUrl(baseUrl).build();
    }

    public void addUpdate(LinkUpdateRequest linkRequest) {
       restClient.post()
            .uri("/updates")
            .contentType(APPLICATION_JSON)
            .body(linkRequest)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
               throw new ResponseException(response.getStatusCode().toString());
           })
           .toBodilessEntity();
    }

}
