package edu.java.scrapper.handler.link;

import edu.java.scrapper.client.GitHubClient;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.jpa.model.Link;
import edu.java.scrapper.model.HandlerData;
import edu.java.scrapper.model.LinkStatus;
import edu.java.scrapper.model.request.RepositoryRequest;
import edu.java.scrapper.model.response.RepositoryEventResponse;
import edu.java.scrapper.model.response.RepositoryResponse;
import edu.java.scrapper.service.ParseService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GitHubHandler extends HandlerLink {

    private final GitHubClient gitHubClient;
    private final ParseService parseService;

    private final LinkRepository linkRepository;

    public GitHubHandler(
        GitHubClient gitHubClient, ParseService parseService,
        @Qualifier("jdbcLinkRepository") LinkRepository linkRepository
    ) {
        this.gitHubClient = gitHubClient;
        this.parseService = parseService;
        this.linkRepository = linkRepository;
    }

    @Override
    public HandlerData handle(String url) {
        if (url.startsWith("https://github.com/")) {
            RepositoryRequest repositoryRequest = parseService.parseUrlToRepositoryRequest(url);
            RepositoryResponse repositoryResponse = gitHubClient.fetchRepository(repositoryRequest);
            return handleUpdates(url, repositoryRequest, repositoryResponse);
        }
        return super.handle(url);
    }

    private HandlerData handleUpdates(
        String url,
        RepositoryRequest repositoryRequest,
        RepositoryResponse repositoryResponse
    ) {
        if (linkRepository.exist(url)) {
            List<RepositoryEventResponse> eventResponses = gitHubClient.fetchRepositoryEvent(repositoryRequest);
            Link link = linkRepository.findByUrl(url).get();
            if (!link.getHashInt().equals(repositoryResponse.hashCode())) {
                String updateMessage = handleMessageUpdates(eventResponses, link);
                if (!updateMessage.isEmpty()) {
                    return new HandlerData(
                        repositoryResponse.hashCode(),
                        LinkStatus.UPDATE,
                        updateMessage
                    );
                }
            }
            return new HandlerData(
                repositoryResponse.hashCode(),
                LinkStatus.NOT_UPDATE,
                "обновлений нет"
            );
        }
        return new HandlerData(
            repositoryResponse.hashCode(),
            LinkStatus.NOT_EXIST,
            "ссылки не существует"
        );
    }

    private String handleMessageUpdates(List<RepositoryEventResponse> eventResponses, Link link) {
        StringBuilder updateMessageBuilder = new StringBuilder();
        for (RepositoryEventResponse response : eventResponses) {
            if (link.getLastCheckTime().isBefore(response.createdAt())) {
                updateMessageBuilder.append(generateUpdateMessage(response));
            }
        }
        return updateMessageBuilder.toString();
    }

    private String generateUpdateMessage(RepositoryEventResponse response) {
        String message = "";
        String opened = "opened";
        if (response.type().equals("IssuesEvent") && response.payload().action().equals(opened)) {
            message = "открылся новый тикет \uD83D\uDD16 \n";
        } else if (response.type().equals("CreateEvent")
            && response.payload().refType().equals("branch")) {
            message = "добавили новую ветку \uD83C\uDF3F \n";
        } else if (response.type().equals("PushEvent") && !response.payload().commits().isEmpty()) {
            message = "добавили новый коммит \uD83D\uDCDD \n";
        } else if (response.type().equals("PullRequestEvent")
            && response.payload().action().equals(opened)) {
            message = "создали новый pull request \uD83D\uDCE6\n";
        } else if (response.type().equals("WatchEvent") && response.payload().action().equals("started")) {
            message = "поставили звезду ⭐\uFE0F\n";
        }
        return message;
    }
}
