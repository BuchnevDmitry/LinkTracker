package edu.java.service.impl;

import edu.java.model.RepositoryRequest;
import edu.java.model.RepositoryResponse;
import edu.java.service.GitHubClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class GitHubClientImpl implements GitHubClient {

    private final WebClient webClient;

    public GitHubClientImpl(
        WebClient.Builder webClientBuilder,
        @Value("https://api.github.com/repos/")
        String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @Override
    public RepositoryResponse fetchRepository(RepositoryRequest request) {
        return this.webClient.get()
            .uri("{username}/{repositoryName}", request.username(), request.repositoryName())
            .retrieve().bodyToMono(RepositoryResponse.class)
            .block();
    }
}
