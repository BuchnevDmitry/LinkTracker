package edu.java.scrapper.service;

import edu.java.scrapper.model.RepositoryRequest;
import edu.java.scrapper.model.RepositoryResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubClientImpl {

    private final WebClient webClient;

    public GitHubClientImpl(
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
