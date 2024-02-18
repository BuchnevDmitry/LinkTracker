package edu.java.scrapper.service;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.model.RepositoryRequest;
import edu.java.scrapper.model.RepositoryResponse;
import edu.java.scrapper.service.impl.GitHubClientImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.OffsetDateTime;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

@WireMockTest
public class GitHubClientTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private GitHubClient gitHubClient;

    @Before
    public void setUp() {
        String baseUrl = wireMockRule.baseUrl();
        gitHubClient = new GitHubClientImpl(WebClient.builder(), baseUrl);
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
        Assertions.assertEquals(response.description(), null);
        Assertions.assertEquals(response.created(), OffsetDateTime.parse("2023-11-08T12:11:22Z"));
        Assertions.assertEquals(response.updated(), OffsetDateTime.parse("2023-11-08T20:01:11Z"));
        Assertions.assertEquals(response.pushed(), OffsetDateTime.parse("2024-01-27T18:23:36Z"));
        Assertions.assertEquals(response.stargazersCount(), 10);
        Assertions.assertEquals(response.watchers(), 100);
        Assertions.assertEquals(response.language(), "Java");
        Assertions.assertEquals(response.forks(), 50);
    }
}
