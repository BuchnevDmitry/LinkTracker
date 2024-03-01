package edu.java.scrapper.service;

import edu.java.scrapper.model.request.RepositoryRequest;
import edu.java.scrapper.model.response.RepositoryResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubClient {

    private final WebClient webClient;

    public GitHubClient(
        WebClient.Builder webClientBuilder,
        @Value("${app.link.git-hub}")
        String baseUrl
    ) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public RepositoryResponse fetchRepository(RepositoryRequest request) {
        return this.webClient.get()
            .uri("{username}/{repositoryName}", request.username(), request.repositoryName())
            .retrieve().bodyToMono(RepositoryResponse.class)
            .block();
    }
}
