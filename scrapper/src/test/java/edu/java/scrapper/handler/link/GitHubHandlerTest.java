package edu.java.scrapper.handler.link;

import edu.java.scrapper.api.exception.BadRequestException;
import edu.java.scrapper.client.GitHubClient;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.model.HandlerData;
import edu.java.scrapper.model.LinkStatus;
import edu.java.scrapper.model.request.RepositoryRequest;
import edu.java.scrapper.model.response.RepositoryEventResponse;
import edu.java.scrapper.model.response.RepositoryResponse;
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
public class GitHubHandlerTest {

    @InjectMocks
    private GitHubHandler gitHubHandler;

    @Mock
    private GitHubClient gitHubClient;

    @Mock
    private LinkRepository linkRepository;

    @Mock
    private ParseService parseService;

    @ParameterizedTest
    @MethodSource("provideUrlAndBooleanForHandle")
    public void handleExecuteExceptionTest(String url) {
        Assertions.assertThrows(BadRequestException.class, () -> gitHubHandler.handle(url));
    }

    private static Stream<Arguments> provideUrlAndBooleanForHandle() {
        return Stream.of(
            Arguments.of("https://adadsasddas/username/repname"),
            Arguments.of("https://noname.com/username/repname")
        );
    }

    @Test
    void handleUpdateExistTest() {
        String username = "username";
        String repName = "repository";
        String url = String.format("https://github.com/%s/%s", username, repName);
        Long linkId = 1L;
        OffsetDateTime time = OffsetDateTime.now();
        RepositoryRequest repositoryRequest = new RepositoryRequest(username, repName);
        RepositoryResponse repositoryResponse = new RepositoryResponse(linkId, username, time, time, time, 10L, 10);
        RepositoryEventResponse.Payload payload = Mockito.mock(RepositoryEventResponse.Payload.class);
        Mockito.when(payload.action()).thenReturn("opened");
        List<RepositoryEventResponse> events = List.of(new RepositoryEventResponse(linkId, "IssuesEvent", payload, time));
        Link link = new Link();
        link.setId(linkId);
        link.setUrl(url);
        link.setCreatedAt(time.minusMinutes(1));
        link.setLastCheckTime(time.minusMinutes(1));
        link.setCreatedBy(username);
        link.setHashInt(repositoryResponse.hashCode() - 1);
        Mockito.when(parseService.parseUrlToRepositoryRequest(url)).thenReturn(repositoryRequest);
        Mockito.when(gitHubClient.fetchRepository(repositoryRequest)).thenReturn(repositoryResponse);
        Mockito.when(gitHubClient.fetchRepositoryEvent(repositoryRequest)).thenReturn(events);
        Mockito.when(linkRepository.exists(url)).thenReturn(true);
        Mockito.when(linkRepository.findByUrl(url)).thenReturn(Optional.of(link));

        HandlerData handlerData = gitHubHandler.handle(url);
        HandlerData handlerDataResult = new HandlerData(1,LinkStatus.UPDATE, "открылся новый тикет \uD83D\uDD16 \n");

        Assertions.assertEquals(handlerDataResult.description(), handlerData.description());
        Assertions.assertEquals(handlerDataResult.typeUpdate(), handlerData.typeUpdate());
    }

    @Test
    void handleLinkIsNotExist() {
        String username = "username";
        String repName = "repository";
        String url = String.format("https://github.com/%s/%s", username, repName);
        Long linkId = 1L;
        OffsetDateTime time = OffsetDateTime.now();
        RepositoryRequest repositoryRequest = new RepositoryRequest(username, repName);
        RepositoryResponse repositoryResponse = new RepositoryResponse(linkId, username, time, time, time, 10L, 10);
        Mockito.when(parseService.parseUrlToRepositoryRequest(url)).thenReturn(repositoryRequest);
        Mockito.when(gitHubClient.fetchRepository(repositoryRequest)).thenReturn(repositoryResponse);
        Mockito.when(linkRepository.exists(url)).thenReturn(false);

        HandlerData handlerData = gitHubHandler.handle(url);
        HandlerData handlerDataResult = new HandlerData(repositoryResponse.hashCode(),LinkStatus.NOT_EXIST, "ссылки не существует");

        Assertions.assertEquals(handlerDataResult.description(), handlerData.description());
        Assertions.assertEquals(handlerDataResult.typeUpdate(), handlerData.typeUpdate());
    }

    @Test
    void handleUpdateIsNotExist() {
        String username = "username";
        String repName = "repository";
        String url = String.format("https://github.com/%s/%s", username, repName);
        Long linkId = 1L;
        OffsetDateTime time = OffsetDateTime.now();
        RepositoryRequest repositoryRequest = new RepositoryRequest(username, repName);
        RepositoryResponse repositoryResponse = new RepositoryResponse(linkId, username, time, time, time, 10L, 10);
        Link link = new Link();
        link.setId(linkId);
        link.setUrl(url);
        link.setCreatedAt(time.minusMinutes(1));
        link.setLastCheckTime(time.minusMinutes(1));
        link.setCreatedBy(username);
        link.setHashInt(repositoryResponse.hashCode());
        Mockito.when(parseService.parseUrlToRepositoryRequest(url)).thenReturn(repositoryRequest);
        Mockito.when(gitHubClient.fetchRepository(repositoryRequest)).thenReturn(repositoryResponse);
        Mockito.when(linkRepository.exists(url)).thenReturn(true);
        Mockito.when(linkRepository.findByUrl(url)).thenReturn(Optional.of(link));

        HandlerData handlerData = gitHubHandler.handle(url);
        HandlerData handlerDataResult = new HandlerData(repositoryResponse.hashCode(),LinkStatus.NOT_UPDATE, "обновлений нет");

        Assertions.assertEquals(handlerDataResult.description(), handlerData.description());
        Assertions.assertEquals(handlerDataResult.typeUpdate(), handlerData.typeUpdate());
    }


}
