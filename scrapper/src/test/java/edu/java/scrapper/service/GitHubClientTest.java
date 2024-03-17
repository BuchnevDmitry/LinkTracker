package edu.java.scrapper.service;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import edu.java.scrapper.client.GitHubClient;
import edu.java.scrapper.model.request.RepositoryRequest;
import edu.java.scrapper.model.response.RepositoryResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.OffsetDateTime;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class GitHubClientTest {

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance().options(wireMockConfig()
            .port(8080))
        .build();

    private GitHubClient gitHubClient;

    @BeforeEach
    public void setUp() {
        String baseUrl = wireMock.baseUrl();
        gitHubClient = new GitHubClient(WebClient.builder(), baseUrl, "token");
    }

    @Test
    public void fetchRepositoryWhenValidJson() {
        String username = "BuchnevDmitry";
        String repositoryName = "twitter";
        stubFor(get(urlEqualTo(String.format("/%s/%s", username, repositoryName)))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                            {
                            "id" : "716078159",
                            "full_name" : "fullName",
                            "description" : null,
                            "created_at" : "2023-11-08T12:11:22Z",
                            "updated_at" : "2023-11-08T20:01:11Z",
                            "pushed_at" : "2024-01-27T18:23:36Z",
                            "stargazers_count" : "10",
                            "watchers" : "100",
                            "language" : "Java",
                            "forks" : "50"
                            }
                          """)));

        RepositoryRequest request = new RepositoryRequest(username, repositoryName);
        RepositoryResponse response = gitHubClient.fetchRepository(request);

        Assertions.assertEquals(response.id(), 716078159);
        Assertions.assertEquals(response.fullName(), "fullName");
        Assertions.assertEquals(response.created(), OffsetDateTime.parse("2023-11-08T12:11:22Z"));
        Assertions.assertEquals(response.updated(), OffsetDateTime.parse("2023-11-08T20:01:11Z"));
        Assertions.assertEquals(response.pushed(), OffsetDateTime.parse("2024-01-27T18:23:36Z"));
        Assertions.assertEquals(response.stargazersCount(), 10);
    }
}
