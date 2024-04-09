package edu.java.bot.client;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import edu.java.bot.model.request.AddChatRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class ScrapperClientTest {

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance().options(wireMockConfig()
            .port(8080))
        .build();

    private ScrapperClient scrapperClient;

    @BeforeEach
    public void setUp() {
        String baseUrl = wireMock.baseUrl();
        scrapperClient = new ScrapperClient(WebClient.builder(), baseUrl, new RetryPolicy());
    }

    @Test
    void retryTestWithInternalErrorException() {
        Long id = 123L;
        stubFor(post(urlEqualTo(String.format("tg-chat/{%d}", id)))
            .willReturn(aResponse()
                .withStatus(501)));
        AddChatRequest request = new AddChatRequest("username");
        Assertions.assertThrows(RuntimeException.class, () -> scrapperClient.registerChat(id, request));
    }

}
