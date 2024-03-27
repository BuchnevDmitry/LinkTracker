package edu.java.scrapper.client;

import edu.java.scrapper.api.exception.BadRequestException;
import edu.java.scrapper.model.request.RepositoryRequest;
import edu.java.scrapper.model.response.RepositoryEventResponse;
import edu.java.scrapper.model.response.RepositoryResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubClient {

    private final WebClient webClient;

    public GitHubClient(
        WebClient.Builder webClientBuilder,
        @Value("${app.link.git-hub-uri}")
        String baseUrl,
        @Value("${app.link.git-hub-token}")
        String token
    ) {
        this.webClient = webClientBuilder.baseUrl(baseUrl)
            .defaultHeader("Authorization", "Bearer " + token)
            .build();
    }

    public RepositoryResponse fetchRepository(RepositoryRequest request) {
        return this.webClient.get()
            .uri("{username}/{repositoryName}", request.username(), request.repositoryName())
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response -> {
                throw new BadRequestException("Ошибка запроса информации о репозитории");
            })
            .bodyToMono(RepositoryResponse.class)
            .block();
    }

    public List<RepositoryEventResponse> fetchRepositoryEvent(RepositoryRequest request) {
        return this.webClient.get()
            .uri("{username}/{repositoryName}/events", request.username(), request.repositoryName())
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response -> {
                throw new BadRequestException("Ошибка запроса информации о событиях в репозитории");
            })
            .bodyToMono(new ParameterizedTypeReference<List<RepositoryEventResponse>>() {
            })
            .block();
    }
}
