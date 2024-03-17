package edu.java.scrapper.handler.link;

import edu.java.scrapper.client.GitHubClient;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.model.HandlerData;
import edu.java.scrapper.model.UpdateStatus;
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
            List<RepositoryEventResponse> eventResponse = gitHubClient.fetchRepositoryEvent(repositoryRequest);
            HandlerData defaultHandlerData = new HandlerData(
                repositoryResponse.hashCode(),
                UpdateStatus.NOT_UPDATE,
                "Обновлений нет"
            );
            if (linkRepository.exist(url)) {
                Link link = linkRepository.findByUrl(url).get();
                if (!link.hashInt().equals(repositoryResponse.hashCode())) {
                    return defaultHandlerData;
                }
                String updateMessage = findUpdates(eventResponse, link);
                if (!updateMessage.isEmpty()) {
                    return new HandlerData(
                        repositoryResponse.hashCode(),
                        UpdateStatus.UPDATE,
                        updateMessage
                    );
                }

            }
            return defaultHandlerData;
        }
        return super.handle(url);
    }

    private String findUpdates(List<RepositoryEventResponse> eventResponses, Link link) {
        StringBuilder updateMessageBuilder = new StringBuilder();
        updateMessageBuilder.append("События:\n");
        for (RepositoryEventResponse response : eventResponses) {
            if (link.lastCheckTime().isBefore(response.createdAt())) {
                updateMessageBuilder.append(getUpdateMessage(response));
            }
        }
        return updateMessageBuilder.toString();
    }

    private String getUpdateMessage(RepositoryEventResponse response) {
        String message = "";
        String opened = "opened";
        if (response.type().equals("IssuesEvent") && response.payload().action().equals(opened)) {
            message = "открылся новый тикет\n";
        } else if (response.type().equals("CreateEvent")
            && response.payload().refType().equals("branch")) {
            message = "добавили новую ветку\n";
        } else if (response.type().equals("PushEvent") && !response.payload().commits().isEmpty()) {
            message = "добавили новый коммит\n";
        } else if (response.type().equals("PullRequestEvent")
            && response.payload().action().equals(opened)) {
            message = "создали новый pull request\n";
        } else if (response.type().equals("WatchEvent") && response.payload().action().equals("started")) {
            message = "в данном репозитории новая звезда";
        }
        return message;
    }
}
