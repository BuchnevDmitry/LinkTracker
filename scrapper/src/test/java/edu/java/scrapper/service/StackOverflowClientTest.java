package edu.java.scrapper.service;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.model.QuestionRequest;
import edu.java.scrapper.model.QuestionResponse;
import edu.java.scrapper.service.impl.StackOverflowClientImpl;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@WireMockTest
public class StackOverflowClientTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private StackOverflowClient stackOverflowClient;

    @Before
    public void setUp() {
        String baseUrl = wireMockRule.baseUrl();
        stackOverflowClient = new StackOverflowClientImpl(WebClient.builder(), baseUrl);
    }

    @Test
    public void fetchQuestionWhenValidJson() {
        String number = "10";
        String order = "desc";
        String sort = "activity";
        String site = "stackoverflow";
        String urlPart = String.format("order=%s&sort=%s&site=%s", order, sort, site);
        stubFor(get(urlEqualTo(String.format("/%s?%s", number, urlPart)))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                            {
                            "items": [
                                    {
                                    "question_id" : "10",
                                    "is_answered" : true,
                                    "view_count" : 10,
                                    "answer_count" : "3",
                                    "score" : "15",
                                    "last_activity_date" : "1708125365",
                                    "creation_date" : "1708125366",
                                    "title" : "question"
                                    }
                                    ]
                            }
                          """)));

        QuestionRequest request = new QuestionRequest(number,order,sort,site);
        QuestionResponse response = stackOverflowClient.fetchQuestion(request);

        Assertions.assertEquals(response.items().get(0).id(), 10);
        Assertions.assertEquals(response.items().get(0).isAnswered(), true);
        Assertions.assertEquals(response.items().get(0).viewCount(), 10);
        Assertions.assertEquals(response.items().get(0).score(), 15);
        Assertions.assertEquals(response.items().get(0).lastActivityDate(), Instant.ofEpochSecond(Long.parseLong("1708125365")).atOffset(ZoneOffset.UTC));
        Assertions.assertEquals(response.items().get(0).creationDate(), Instant.ofEpochSecond(Long.parseLong("1708125366")).atOffset(ZoneOffset.UTC));
        Assertions.assertEquals(response.items().get(0).title(), "question");
    }
}
