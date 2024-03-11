package edu.java.scrapper.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.scrapper.api.exception.BadRequestException;
import edu.java.scrapper.model.request.LinkUpdateRequest;
import edu.java.scrapper.model.response.ApiErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class BotClient {

    private final RestClient restClient;

    @Autowired
    private ObjectMapper objectMapper;

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
                ApiErrorResponse apiErrorResponse = objectMapper.readValue(response.getBody(), ApiErrorResponse.class);
                throw new BadRequestException(apiErrorResponse.description());
           })
           .toBodilessEntity();
    }

}
