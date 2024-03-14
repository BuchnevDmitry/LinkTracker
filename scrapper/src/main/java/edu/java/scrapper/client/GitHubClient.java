package edu.java.scrapper.client;

import edu.java.scrapper.api.exception.BadRequestException;
import edu.java.scrapper.model.request.RepositoryRequest;
import edu.java.scrapper.model.response.RepositoryResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubClient {

    private final WebClient webClient;

    public GitHubClient(
        WebClient.Builder webClientBuilder,
        @Value("${app.url.git-hub}")
        String baseUrl
    ) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public RepositoryResponse fetchRepository(RepositoryRequest request) {
        return this.webClient.get()
            .uri("{username}/{repositoryName}", request.username(), request.repositoryName())
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response -> {
                throw new BadRequestException("Ошибка запроса информации о резозитории");
            })
            .bodyToMono(RepositoryResponse.class)
            .block();
    }
}
