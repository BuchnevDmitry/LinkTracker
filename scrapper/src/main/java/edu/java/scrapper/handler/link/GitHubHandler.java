package edu.java.scrapper.handler.link;

import edu.java.scrapper.client.GitHubClient;
import edu.java.scrapper.model.request.RepositoryRequest;
import edu.java.scrapper.model.response.RepositoryResponse;
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
    public Integer handle(String url) {
        if (url.startsWith("https://github.com/")) {
            RepositoryRequest repositoryRequest = parseService.parseUrlToRepositoryRequest(url);
            RepositoryResponse repositoryResponse = gitHubClient.fetchRepository(repositoryRequest);
            return repositoryResponse.hashCode();
        }
        return super.handle(url);
    }
}
