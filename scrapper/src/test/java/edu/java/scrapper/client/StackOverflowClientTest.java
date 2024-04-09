package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import edu.java.scrapper.model.request.QuestionRequest;
import edu.java.scrapper.model.response.AnswerResponse;
import edu.java.scrapper.model.response.QuestionResponse;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class StackOverflowClientTest {

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance().options(wireMockConfig()
            .port(8080))
        .build();

    private StackOverflowClient stackOverflowClient;

    @BeforeEach
    public void setUp() {
        String baseUrl = wireMock.baseUrl();
        stackOverflowClient = new StackOverflowClient(WebClient.builder(), baseUrl, new RetryPolicy());
    }

    @Test
    public void fetchQuestionWithValidJson() {
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

        QuestionRequest request = new QuestionRequest(number, order, sort, site);
        QuestionResponse response = stackOverflowClient.fetchQuestion(request);

        Assertions.assertEquals(response.items().get(0).id(), 10);
        Assertions.assertEquals(
            response.items().get(0).lastActivityDate(),
            Instant.ofEpochSecond(Long.parseLong("1708125365")).atOffset(ZoneOffset.UTC)
        );
        Assertions.assertEquals(
            response.items().get(0).creationDate(),
            Instant.ofEpochSecond(Long.parseLong("1708125366")).atOffset(ZoneOffset.UTC)
        );
    }

    @Test
    public void fetchQuestionAnswerWithValidJson() {
        String number = "10";
        String order = "desc";
        String sort = "creation";
        String site = "stackoverflow";
        String urlPart = String.format("order=%s&sort=%s&site=%s", order, sort, site);
        stubFor(get(urlEqualTo(String.format("/%s/answers?%s", number, urlPart)))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                      {
                          "items": [
                              {
                                  "owner": {
                                      "account_id": 7141463,
                                      "reputation": 17116,
                                      "user_id": 5458913,
                                      "user_type": "registered",
                                      "accept_rate": 71,
                                      "profile_image": "https://i.stack.imgur.com/agLDL.png?s=256&g=1",
                                      "display_name": "dshukertjr",
                                      "link": "https://stackoverflow.com/users/5458913/dshukertjr"
                                  },
                                  "is_accepted": false,
                                  "score": 1,
                                  "last_activity_date": 1710738709,
                                  "creation_date": 1710738709,
                                  "answer_id": 78178063,
                                  "question_id": 78175142,
                                  "content_license": "CC BY-SA 4.0"
                              }
                          ],
                          "has_more": false,
                          "quota_max": 300,
                          "quota_remaining": 296
                      }
                    """)));

        QuestionRequest request = new QuestionRequest(number, order, sort, site);
        AnswerResponse response = stackOverflowClient.fetchQuestionAnswer(request);

        Assertions.assertEquals(response.items().get(0).creationDate(), Instant.ofEpochSecond(Long.parseLong("1710738709")).atOffset(ZoneOffset.UTC));
    }

    @Test
    void retryTest() {
        String number = "10";
        String order = "desc";
        String sort = "creation";
        String site = "stackoverflow";
        String urlPart = String.format("order=%s&sort=%s&site=%s", order, sort, site);
        stubFor(get(urlEqualTo(String.format("/%s/answers?%s", number, urlPart)))
            .willReturn(aResponse()
                    .withStatus(501)));
        QuestionRequest request = new QuestionRequest(number, order, sort, site);
        Assertions.assertThrows(RuntimeException.class, () -> stackOverflowClient.fetchQuestionAnswer(request));
    }

}
