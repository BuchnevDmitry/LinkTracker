package edu.java.scrapper.handler.link;

import edu.java.scrapper.api.exception.BadRequestException;
import edu.java.scrapper.client.StackOverflowClient;
import edu.java.scrapper.model.request.QuestionRequest;
import edu.java.scrapper.model.response.QuestionResponse;
import edu.java.scrapper.service.ParseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class StackOverflowHandlerTest {

    @InjectMocks
    private StackOverflowHandler stackOverflowHandler;
    @Mock
    private StackOverflowClient stackOverflowClient;

    @Spy
    private ParseService parseService;

    @ParameterizedTest
    @MethodSource("provideUrlAndBooleanForHandle")
    public void methodHandleTest(String url) {
        QuestionRequest request = parseService.parseUrlToQuestionRequest(url);
        QuestionResponse response = Mockito.mock(QuestionResponse.class);
        Mockito.lenient().when(stackOverflowClient.fetchQuestion(request)).thenReturn(response);

        Assertions.assertThrows(BadRequestException.class, () -> stackOverflowHandler.handle(url));

    }

    private static Stream<Arguments> provideUrlAndBooleanForHandle() {
        return Stream.of(
            Arguments.of("https://reegwgregw/questions/78128827/name"),
            Arguments.of("https://noname.com/questions/78128827/name")
        );
    }
}
