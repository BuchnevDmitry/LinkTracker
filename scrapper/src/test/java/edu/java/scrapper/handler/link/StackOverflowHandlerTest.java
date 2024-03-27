package edu.java.scrapper.handler.link;

import edu.java.scrapper.api.exception.BadRequestException;
import edu.java.scrapper.client.StackOverflowClient;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.model.HandlerData;
import edu.java.scrapper.model.LinkStatus;
import edu.java.scrapper.model.request.QuestionRequest;
import edu.java.scrapper.model.response.AnswerResponse;
import edu.java.scrapper.model.response.QuestionResponse;
import edu.java.scrapper.service.ParseService;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StackOverflowHandlerTest {

    @InjectMocks
    private StackOverflowHandler stackOverflowHandler;
    @Mock
    private StackOverflowClient stackOverflowClient;

    @Mock
    private LinkRepository linkRepository;

    @Mock
    private ParseService parseService;

    @ParameterizedTest
    @MethodSource("provideUrlAndBooleanForHandle")
    public void handleExecuteExceptionTest(String url) {
        Assertions.assertThrows(BadRequestException.class, () -> stackOverflowHandler.handle(url));

    }

    private static Stream<Arguments> provideUrlAndBooleanForHandle() {
        return Stream.of(
            Arguments.of("https://reegwgregw/questions/78128827/name"),
            Arguments.of("https://noname.com/questions/78128827/name")
        );
    }

    @Test
    public void handleUpdateExistTest() throws URISyntaxException {
        Long linkId = 1L;
        String num = "3";
        String url = String.format("https://stackoverflow.com/%s", 3);
        OffsetDateTime time = OffsetDateTime.now();

        QuestionRequest questionRequest = new QuestionRequest(num);
        QuestionResponse questionResponse = new QuestionResponse(List.of(new QuestionResponse.ItemResponse(linkId, 10, time, time)));
        AnswerResponse answerResponse = new AnswerResponse(List.of(new AnswerResponse.ItemResponse(time)));
        Link link = new Link();
        link.setId(linkId);
        link.setUrl(url);
        link.setCreatedAt(time.minusMinutes(1));
        link.setLastCheckTime(time.minusMinutes(1));
        link.setCreatedBy("username");
        link.setHashInt(questionRequest.hashCode() - 1);
        Mockito.when(parseService.parseUrlToQuestionRequest(url)).thenReturn(questionRequest);
        Mockito.when(stackOverflowClient.fetchQuestion(questionRequest)).thenReturn(questionResponse);
        questionRequest.setSort("creation");
        Mockito.when(stackOverflowClient.fetchQuestionAnswer(questionRequest)).thenReturn(answerResponse);
        Mockito.when(linkRepository.exists(url)).thenReturn(true);
        Mockito.when(linkRepository.findByUrl(url)).thenReturn(Optional.of(link));

        HandlerData handlerData = stackOverflowHandler.handle(url);
        HandlerData handlerDataResult = new HandlerData(1, LinkStatus.UPDATE, "появился новый ответ \uD83D\uDD14 \n");

        Assertions.assertEquals(handlerDataResult.description(), handlerData.description());
        Assertions.assertEquals(handlerDataResult.typeUpdate(), handlerData.typeUpdate());
    }

    @Test
    public void handleUpdateIsNotExistTest() throws URISyntaxException {
        Long linkId = 1L;
        String num = "3";
        String url = String.format("https://stackoverflow.com/%s", 3);
        OffsetDateTime time = OffsetDateTime.now();

        QuestionRequest questionRequest = new QuestionRequest(num);
        QuestionResponse questionResponse = new QuestionResponse(List.of(new QuestionResponse.ItemResponse(linkId, 10, time, time)));
        Link link = new Link();
        link.setId(linkId);
        link.setUrl(url);
        link.setCreatedAt(time.minusMinutes(1));
        link.setLastCheckTime(time.minusMinutes(1));
        link.setCreatedBy("username");
        link.setHashInt(questionResponse.hashCode());
        Mockito.when(parseService.parseUrlToQuestionRequest(url)).thenReturn(questionRequest);
        Mockito.when(stackOverflowClient.fetchQuestion(questionRequest)).thenReturn(questionResponse);
        questionRequest.setSort("creation");
        Mockito.when(linkRepository.exists(url)).thenReturn(true);
        Mockito.when(linkRepository.findByUrl(url)).thenReturn(Optional.of(link));

        HandlerData handlerData = stackOverflowHandler.handle(url);
        HandlerData handlerDataResult = new HandlerData(1, LinkStatus.NOT_UPDATE, "обновлений нет");

        Assertions.assertEquals(handlerDataResult.description(), handlerData.description());
        Assertions.assertEquals(handlerDataResult.typeUpdate(), handlerData.typeUpdate());
    }

    @Test
    public void handleLinkIsNotExistTest() {
        Long linkId = 1L;
        String num = "3";
        String url = String.format("https://stackoverflow.com/%s", 3);
        OffsetDateTime time = OffsetDateTime.now();

        QuestionRequest questionRequest = new QuestionRequest(num);
        QuestionResponse questionResponse = new QuestionResponse(List.of(new QuestionResponse.ItemResponse(linkId, 10, time, time)));
        Mockito.when(parseService.parseUrlToQuestionRequest(url)).thenReturn(questionRequest);
        Mockito.when(stackOverflowClient.fetchQuestion(questionRequest)).thenReturn(questionResponse);
        Mockito.when(linkRepository.exists(url)).thenReturn(false);

        HandlerData handlerData = stackOverflowHandler.handle(url);
        HandlerData handlerDataResult = new HandlerData(1, LinkStatus.NOT_EXIST, "ссылки не существует");

        Assertions.assertEquals(handlerDataResult.description(), handlerData.description());
        Assertions.assertEquals(handlerDataResult.typeUpdate(), handlerData.typeUpdate());
    }
}
