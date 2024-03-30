package edu.java.scrapper.client;

import edu.java.scrapper.api.exception.BadRequestException;
import edu.java.scrapper.api.exception.InternalServerErrorException;
import edu.java.scrapper.model.request.RepositoryRequest;
import edu.java.scrapper.model.response.RepositoryEventResponse;
import edu.java.scrapper.model.response.RepositoryResponse;
import java.time.Duration;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@SuppressWarnings("MagicNumber")
@Component
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
            .onStatus(HttpStatusCode::is5xxServerError, response -> {
                throw new InternalServerErrorException("Ошибка сервера при запросе информации о репозитории");
            })
            .bodyToMono(RepositoryResponse.class)
            .retryWhen(RetryUtil.linear(Duration.ofSeconds(2), 3, List.of(InternalServerErrorException.class)))
            .block();
    }

    public List<RepositoryEventResponse> fetchRepositoryEvent(RepositoryRequest request) {
        return this.webClient.get()
            .uri("{username}/{repositoryName}/events", request.username(), request.repositoryName())
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response -> {
                throw new BadRequestException("Ошибка запроса информации о событиях в репозитории");
            })
            .onStatus(HttpStatusCode::is5xxServerError, response -> {
                throw new InternalServerErrorException("""
                    Ошибка сервера при запросе информации о событиях в репозитории
                    """);
            })
            .bodyToMono(new ParameterizedTypeReference<List<RepositoryEventResponse>>() {
            })
            .retryWhen(RetryUtil.linear(Duration.ofSeconds(2), 3, List.of(InternalServerErrorException.class)))
            .block();
    }
}
