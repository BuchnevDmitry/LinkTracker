package edu.java.scrapper.service;

import edu.java.scrapper.model.RepositoryResponse;
import edu.java.scrapper.model.RepositoryRequest;

public interface GitHubClient {
    RepositoryResponse fetchRepository(RepositoryRequest request);
}
