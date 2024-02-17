package edu.java.service;

import edu.java.model.RepositoryRequest;
import edu.java.model.RepositoryResponse;

public interface GitHubClient {
    RepositoryResponse fetchRepository(RepositoryRequest request);
}
