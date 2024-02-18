package edu.java.scrapper.service;

import edu.java.scrapper.model.RepositoryRequest;
import edu.java.scrapper.model.RepositoryResponse;

public interface GitHubClient {
    RepositoryResponse fetchRepository(RepositoryRequest request);
}
