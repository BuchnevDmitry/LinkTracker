package edu.java.scrapper.handler.link;

import edu.java.scrapper.client.GitHubClient;
import edu.java.scrapper.model.request.RepositoryRequest;
import edu.java.scrapper.service.ParseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GitHubHandler extends HandlerLink {

    private final GitHubClient gitHubClient;
    private final ParseService parseService;

    public GitHubHandler(GitHubClient gitHubClient, ParseService parseService) {
        this.gitHubClient = gitHubClient;
        this.parseService = parseService;
    }

    @Override
    public boolean handle(String url) {
        if (url.startsWith("https://github.com/")) {
            RepositoryRequest repositoryRequest = parseService.parseUrlToRepositoryRequest(url);
            gitHubClient.fetchRepository(repositoryRequest);
            return true;
        }
        return super.handle(url);
    }
}
