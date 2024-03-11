package edu.java.scrapper.handler.link;

import edu.java.scrapper.client.GitHubClient;
import edu.java.scrapper.model.request.RepositoryRequest;
import edu.java.scrapper.model.response.RepositoryResponse;
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
public class GitHubHandlerTest {

    @InjectMocks
    private GitHubHandler gitHubHandler;

    @Mock
    private GitHubClient gitHubClient;

    @Spy
    private ParseService parseService;

    @ParameterizedTest
    @MethodSource("provideUrlAndBooleanForHandle")
    public void methodHandleTest(String url, boolean resultOutput) {
        RepositoryRequest request = parseService.parseUrlToRepositoryRequest(url);
        RepositoryResponse response = Mockito.mock(RepositoryResponse.class);
        Mockito.lenient().when(gitHubClient.fetchRepository(request)).thenReturn(response);

        boolean resultHandle = gitHubHandler.handle(url);

        Assertions.assertEquals(resultHandle, resultOutput);

    }

    private static Stream<Arguments> provideUrlAndBooleanForHandle() {
        return Stream.of(
            Arguments.of("https://github.com/username/repname", true),
            Arguments.of("https://noname.com/username/repname", false)
        );
    }


}
